package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.donation.dto.DonationDto;
import br.org.oficinadasmeninas.domain.payment.dto.CheckoutNotificationDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentNotificationDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.PaymentChargesDto;
import br.org.oficinadasmeninas.infra.logspagbank.dto.CreateLogPagbank;
import br.org.oficinadasmeninas.infra.logspagbank.service.LogPagbankService;
import br.org.oficinadasmeninas.presentation.exceptions.InternalException;
import br.org.oficinadasmeninas.presentation.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.service.IPaymentService;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.ResponseCreateCheckoutDto;
import br.org.oficinadasmeninas.domain.paymentgateway.service.IPaymentGatewayService;
import br.org.oficinadasmeninas.domain.sponsorship.Sponsorship;
import br.org.oficinadasmeninas.domain.sponsorship.dto.SponsorshipDto;
import br.org.oficinadasmeninas.domain.sponsorship.dto.UpdateSponsorshipDto;
import br.org.oficinadasmeninas.domain.sponsorship.service.ISponsorshipService;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.RequestCreateCheckoutConfig;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.RequestCreateCheckoutPagbank;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.RequestCreateCheckoutRecurrenceInterval;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.RequestSubscriptionIdCustomer;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseCreateCheckoutLink;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseCreateCheckoutPagbank;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseFindSubscriptionId;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseFindSubscriptionIdSubscription;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseSignatureCustomer;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseWebhookCustomer;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.mappers.RequestCreateCheckoutPagbankMapper;
import br.org.oficinadasmeninas.infra.shared.exception.PaymentGatewayException;
import br.org.oficinadasmeninas.infra.user.service.UserService;
import br.org.oficinadasmeninas.presentation.shared.utils.IsoDateFormater;

@Service
public class PaymentGatewayService implements IPaymentGatewayService {
    private final RequestCreateCheckoutPagbankMapper mapper;

    private final WebClient webClient;

    private final WebClient webClientSubscription;

    private final IDonationService donationService;

    private final IPaymentService paymentService;

    private final ISponsorshipService sponsorshipService;

    private final LogPagbankService logService;

    private final UserService userService;

    @Value("${app.redirectCheckoutUrl}")
    private String redirectUrl;

    @Value("${app.notification_urls}")
    private String notificationUrls;

    @Value("${app.payment_notification_urls}")
    private String paymentNotificationUrls;

    @Value("${app.url}")
    private String url;

    @Value("${app.url_signature}")
    private String urlSignature;

    @Value("${app.token}")
    private String token;



    public PaymentGatewayService (
            RequestCreateCheckoutPagbankMapper mapper,
            WebClient.Builder builder,
            @Value("${app.url}") String url,
            @Value("${app.url_signature}") String urlSignature,
            IDonationService donationService, IPaymentService paymentService,
            ISponsorshipService sponsorshipService,
            UserService userService,
            LogPagbankService logService
    ) {
        this.mapper = mapper;
		this.donationService = donationService;
        this.paymentService = paymentService;
        this.sponsorshipService = sponsorshipService;
        this.webClient = builder.baseUrl(url)
                .build();
        this.webClientSubscription = builder.baseUrl(urlSignature)
                .build();
        this.userService = userService;
        this.logService = logService;
    }

    @Override
    public void cancelCheckout(String checkoutId) {
        try {
            StringBuilder uriBuilder = new StringBuilder()
                    .append("/checkouts/")
                    .append(checkoutId)
                    .append("/inactivate");

            var response = webClient.post()
                    .uri(uriBuilder.toString())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(ResponseSignatureCustomer.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new InternalException(e.getStatusCode() + " " + e.getStatusText() + e.getResponseBodyAs(String.class));
        }
    }


    @Override
    public ResponseCreateCheckoutDto createCheckout(RequestCreateCheckoutDto requestCreateCheckoutDto) {


        RequestCreateCheckoutConfig defaults = new RequestCreateCheckoutConfig(
                IsoDateFormater.addHours(4),
                redirectUrl,
                requestCreateCheckoutDto.signatureDto().isRecurrence()? "APADRINHAMENTO OFICINA DAS MENINAS": "DOAÇÃO OFICINA DAS MENINAS",
                1,
                List.of(notificationUrls),
                List.of(paymentNotificationUrls),
                requestCreateCheckoutDto.signatureDto().isRecurrence()? List.of(PaymentMethodEnum.CREDIT_CARD) : List.of(PaymentMethodEnum.CREDIT_CARD, PaymentMethodEnum.DEBIT_CARD, PaymentMethodEnum.PIX),
                "https://dev.apollomusic.com.br/logo_preenchimento_branco.png",
                12,
                new RequestCreateCheckoutRecurrenceInterval(
                        "MONTH",
                        1
                )
        );

        RequestCreateCheckoutPagbank pagbankRequest = mapper.toGateway(requestCreateCheckoutDto,defaults);

        ResponseCreateCheckoutPagbank responseCreateCheckoutPagbank = createPagBankCheckout(pagbankRequest);

        String link = responseCreateCheckoutPagbank.links()
                .stream()
                .findFirst()
                .map(ResponseCreateCheckoutLink::href)
                .orElseThrow(() -> new PaymentGatewayException("Link de pagamento não encontrado"));

        return new ResponseCreateCheckoutDto(link, responseCreateCheckoutPagbank.id(), requestCreateCheckoutDto.internalId());
    }

    @Override
    public void updatePaymentStatus(UUID donationId, PaymentStatusEnum paymentStatus, PaymentMethodEnum paymentMethod, boolean recurring, ResponseWebhookCustomer customer) {

        List<PaymentDto> payments = paymentService.findByDonationId(donationId);

        donationService.updateMethod(donationId, paymentMethod);

        if (payments == null || payments.isEmpty()) {
            throw new ValidationException("Não foi possível encontrar doação");
        }

        PaymentDto payment = payments.getLast();

        paymentService.updatePaymentDate(payment.id(), LocalDateTime.now());
        paymentService.updateStatus(payment.id(), paymentStatus);

        if (recurring) {
        	String subscriptionId = this.findSubscriptionId( new RequestSubscriptionIdCustomer(customer.name(), customer.tax_id()));
            UserDto userDto = userService.findByDocument(customer.tax_id());

            SponsorshipDto sponsor = sponsorshipService.findByUserId(userDto.getId()).getLast();

            sponsorshipService.update(new UpdateSponsorshipDto(sponsor.id(), null, true, subscriptionId));
        }
    }

    @Override
    public void updateCheckoutStatus(String checkoutId, UUID donationId, PaymentStatusEnum paymentStatus) {
        List<PaymentDto> payments = paymentService.findByDonationId(donationId);

        if (payments != null && !payments.isEmpty()) {
            PaymentDto payment = payments.getLast();
            paymentService.updateStatus(payment.id(), paymentStatus);
        }
    }
    private ResponseCreateCheckoutPagbank createPagBankCheckout(RequestCreateCheckoutPagbank checkoutPagbank) {
        try {
			var response = webClient.post()
                    .uri("/checkouts")
                    .header("Authorization", "Bearer " + token)
                    .bodyValue(checkoutPagbank)
                    .retrieve()
                    .bodyToMono(ResponseCreateCheckoutPagbank.class)
                    .block();

            Optional<ResponseCreateCheckoutLink> payLink = Objects.requireNonNull(response).links().stream()
                    .filter(link -> "PAY".equalsIgnoreCase(link.rel()))
                    .findFirst();

            return new ResponseCreateCheckoutPagbank(
                    response.id(),
                    payLink.map(List::of).orElse(List.of()),
                    response.status()
            );
        } catch (WebClientResponseException e) {
            throw new InternalException(e.getRawStatusCode() + " " + e.getStatusText());
        }
    }

	@Override
	public String findSubscriptionId(RequestSubscriptionIdCustomer customer) {
		try {

			var response = webClientSubscription.get()
					.uri("/subscriptions")
                    .header("Authorization", "Bearer " + token)
					.header("q", customer.document())
					.header("q", customer.name())
					.retrieve()
					.bodyToMono(ResponseFindSubscriptionId.class)
					.block();

			return response.subscriptions().getFirst().id();

		} catch (Exception e) {
			throw new InternalException(e.toString());
		}
	}

    @Override
    public void cancelRecurringDonationSubscription(String subscriptionId) {
        try {
            String uriBuilder = "/subscriptions/" +
                    subscriptionId +
                    "/cancel";

            webClientSubscription.put()
                    .uri(uriBuilder)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(ResponseSignatureCustomer.class)
                    .block();


        } catch (WebClientResponseException e) {
            throw new InternalException(e.getStatusCode() + " " + e.getStatusText() + e.getResponseBodyAs(String.class));
        }
    }


    @Override
    public void notifyPayment(PaymentNotificationDto request) {
        PaymentChargesDto charge = request.charges().getFirst();
        boolean recurring = charge.recurring() != null;
        ResponseWebhookCustomer customer = request.customer();
        updatePaymentStatus(request.reference_id(), charge.status(), charge.payment_method().type(), recurring, customer);


        if (charge.status() == PaymentStatusEnum.PAID){
            DonationDto donation = donationService.findById(request.reference_id());
            if (donation.checkoutId() != null) {
                cancelCheckout(donation.checkoutId());
                paymentService.cancelPendingPaymentByDonationId(donation.id());
            }
        }
    }

    @Override
    public void notifyCheckout(CheckoutNotificationDto request) {
        updateCheckoutStatus(request.id(), request.reference_id(), request.status());
    }

    private void saveLog(Object object) throws IOException {
        logService.createLogPagbank(new CreateLogPagbank(
                "WEBHOOK NOTIFY BODY",
                LocalDateTime.now(),
                object
        ));
    }
}
