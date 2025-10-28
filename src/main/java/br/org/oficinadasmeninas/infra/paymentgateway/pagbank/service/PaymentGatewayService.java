package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.service.IPaymentService;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.ResponseCreateCheckoutDto;
import br.org.oficinadasmeninas.domain.paymentgateway.service.IPaymentGatewayService;
import br.org.oficinadasmeninas.domain.sponsor.service.ISponsorService;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
 import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.RequestCreateCheckoutConfig;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.RequestCreateCheckoutPagbank;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.RequestCreateCheckoutRecurrenceInterval;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseCreateCheckoutLink;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseCreateCheckoutPagbank;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseSignatureCustomer;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseWebhookCustomer;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.mappers.RequestCreateCheckoutPagbankMapper;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.mappers.RequestNotifyPaymentDonationStatusMapper;
import br.org.oficinadasmeninas.infra.shared.exception.PaymentGatewayException;
import br.org.oficinadasmeninas.infra.user.service.UserService;
import br.org.oficinadasmeninas.presentation.shared.utils.IsoDateFormater;

@Service
public class PaymentGatewayService implements IPaymentGatewayService {
    private final RequestCreateCheckoutPagbankMapper mapper;

    private final WebClient webClient;

    private final IDonationService donationService;

    private final IPaymentService paymentService;

    private final ISponsorService sponsorService;
    private final UserService userService;

    @Value("${app.redirectCheckoutUrl}")
    private String redirectUrl;

    @Value("${app.notification_urls}")
    private String notificationUrls;

    @Value("${app.payment_notification_urls}")
    private String paymentNotificationUrls;

    @Value("${app.url}")
    private String url;

    @Value("${app.token}")
    private String token;



    public PaymentGatewayService(RequestCreateCheckoutPagbankMapper mapper, WebClient.Builder builder, @Value("${app.url}") String url, IDonationService donationService, IPaymentService paymentService, ISponsorService sponsorService, UserService userService) {
        this.mapper = mapper;
        this.donationService = donationService;
        this.paymentService = paymentService;
        this.sponsorService = sponsorService;
        this.webClient = builder.baseUrl(url)
                .build();
        this.userService = userService;
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

            System.out.println(response);
        } catch (WebClientResponseException e) {
            throw new PaymentGatewayException(e.getStatusCode() + " " + e.getStatusText() + e.getResponseBodyAs(String.class));
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
                List.of(PaymentMethodEnum.CREDIT_CARD),
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

        return new ResponseCreateCheckoutDto(link, responseCreateCheckoutPagbank.id());
    }

    @Override
    public void updatePaymentStatus(UUID paymentId, PaymentStatusEnum paymentStatus, PaymentMethodEnum paymentMethod, boolean recurring, ResponseWebhookCustomer customer) {
       DonationStatusEnum donationStatusEnum = RequestNotifyPaymentDonationStatusMapper.fromPaymentStatus(paymentStatus);
       donationService.updateStatus(paymentId, donationStatusEnum);
       paymentService.updateStatus(paymentId, paymentStatus);
       paymentService.updateMethod(paymentId, paymentMethod);


       if (recurring) {
         UserDto userDto = userService.findByDocument(customer.tax_id());
         sponsorService.activateByUserId(userDto.getId());
       }
    }

    @Override
    public void updateCheckoutStatus(String checkoutId, UUID paymentId, PaymentStatusEnum paymentStatus) {
        DonationStatusEnum donationStatusEnum = RequestNotifyPaymentDonationStatusMapper.fromPaymentStatus(paymentStatus);
        donationService.updateStatus(paymentId, donationStatusEnum);
        paymentService.updateStatus(paymentId, paymentStatus);
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
            throw new PaymentGatewayException(e.getRawStatusCode() + " " + e.getStatusText());
        }
    }
}
