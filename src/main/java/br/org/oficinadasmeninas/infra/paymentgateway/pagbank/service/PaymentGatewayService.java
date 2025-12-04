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
import br.org.oficinadasmeninas.domain.sponsorship.dto.SponsorshipDto;
import br.org.oficinadasmeninas.domain.sponsorship.dto.UpdateSponsorshipDto;
import br.org.oficinadasmeninas.domain.sponsorship.service.ISponsorshipService;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.RequestCalculateFeesDto;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.RequestCreateCheckoutConfig;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.RequestCreateCheckoutPagbank;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.RequestCreateCheckoutRecurrenceInterval;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.RequestSubscriptionIdCustomer;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseCalculateFeesDto;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseCreateCheckoutLink;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseCreateCheckoutPagbank;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseFindSubscriptionId;
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

        saveLog("CREATE CHECKOUT", pagbankRequest);

        ResponseCreateCheckoutPagbank responseCreateCheckoutPagbank = createPagBankCheckout(pagbankRequest);

        String link = responseCreateCheckoutPagbank.links()
                .stream()
                .findFirst()
                .map(ResponseCreateCheckoutLink::href)
                .orElseThrow(() -> new PaymentGatewayException("Link de pagamento não encontrado"));

        return new ResponseCreateCheckoutDto(link, responseCreateCheckoutPagbank.id(), requestCreateCheckoutDto.internalId());
    }

    @Override
    public void updatePaymentStatus(UUID donationId, PaymentStatusEnum paymentStatus, PaymentMethodEnum paymentMethod, String cardBrand, boolean recurring, ResponseWebhookCustomer customer) {

        List<PaymentDto> payments = paymentService.findByDonationId(donationId);

        donationService.updateMethod(donationId, paymentMethod);

        if (payments == null || payments.isEmpty()) {
            throw new ValidationException("Não foi possível encontrar doação");
        }

        PaymentDto payment = payments.getLast();

        paymentService.updatePaymentDate(payment.id(), LocalDateTime.now());
        paymentService.updateStatus(payment.id(), paymentStatus);

        donationService.updateMethod(donationId, paymentMethod);

        if (cardBrand != null && !cardBrand.isBlank()) {
            donationService.updateCardBrand(donationId, cardBrand);
        }

        this.saveLog("PAYMENT STATUS UPDATED",
                String.format("Donation: %s, From: %s, To: %s, Method: %s, Card Brand: %s, Recurring: %s",
                        donationId, payment.status(), paymentStatus, paymentMethod, cardBrand, recurring));

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
            PaymentStatusEnum currentStatus = payment.status();

            boolean isTerminalStatus = currentStatus == PaymentStatusEnum.PAID ||
                                      currentStatus == PaymentStatusEnum.CANCELED ||
                                      currentStatus == PaymentStatusEnum.DECLINED;


            if (isTerminalStatus) {
                saveLog("CHECKOUT STATUS UPDATE IGNORED - Terminal Status",
                        String.format("Checkout: %s, Current: %s, Attempted: %s",
                                checkoutId, currentStatus, paymentStatus));
                return;
            }

            if (currentStatus == PaymentStatusEnum.ACTIVE &&
                    paymentStatus == PaymentStatusEnum.EXPIRED) {
                saveLog("CHECKOUT STATUS UPDATE IGNORED - Active to Expired",
                        String.format("Checkout: %s, Status: %s", checkoutId, currentStatus));
                return;
            }

            saveLog("CHECKOUT STATUS UPDATED",
                    String.format("Checkout: %s, From: %s, To: %s",
                            checkoutId, currentStatus, paymentStatus));

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

            this.saveLog("FIND SUBSCRIPTION ID RESPONSE", response);

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
        this.saveLog("WEBHOOK NOTIFY BODY", request);
        PaymentChargesDto charge = request.charges().getFirst();
        boolean recurring = charge.recurring() != null;
        ResponseWebhookCustomer customer = request.customer();
        updatePaymentStatus(request.reference_id(), charge.status(), charge.payment_method().type(),
                charge.payment_method().card() != null ? charge.payment_method().card().brand() : null, recurring,
                customer);

        if (charge.status() == PaymentStatusEnum.PAID){
            DonationDto donation = donationService.findById(request.reference_id());

            Double fee = calculateFee(donation.value(), charge.payment_method().type(), donation.cardBrand());
            double netValue = donation.value() - fee;
            donationService.updateFeeAndLiquidValue(donation.id(), fee, netValue);

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

    private void saveLog(String label, Object object){
        logService.createLogPagbank(new CreateLogPagbank(
                label,
                LocalDateTime.now(),
                object
        ));
    }

    @Override
    public ResponseCalculateFeesDto fetchTransactionFees(RequestCalculateFeesDto request) {
        // TODO: comentado temporariamente até ter homologacao da API de taxas do PagBank
        /*
        try {
            return webClient.get()
                    .uri(uriBuilder -> {
                        UriBuilder builder = uriBuilder.path("/charges/fees/calculate");

                        if (request.paymentMethods() != null && !request.paymentMethods().isEmpty()) {
                            for (String method : request.paymentMethods()) {
                                builder.queryParam("payment_methods[]", method);
                            }
                        }

                        if (request.value() != null) {
                            builder.queryParam("value", request.value());
                        }

                        builder.queryParam("max_installments", request.maxInstallmentsNoInterest());

                        return builder.build();
                    })
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .retrieve()
                    .bodyToMono(ResponseCalculateFeesDto.class)
                    .block();

        } catch (WebClientResponseException e) {
            throw new PaymentGatewayException(
                    e.getStatusCode() + " " + e.getStatusText() + " " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new PaymentGatewayException("Erro ao calcular taxas: " + e.getMessage());
        }
        */
        return null;
    }

    /**
     *
     * Taxas fixas
     * - DÉBITO: 2,39%
     * - CRÉDITO: 3,99% + R$ 0,40
     * - PIX: 1,89%
     * - BOLETO: R$ 1,89
     */
    private Double extractFeeFromResponse(ResponseCalculateFeesDto response, PaymentMethodEnum paymentMethod, String cardBrand, double donationValue) {
        // TODO: comentado temporariamente até ter homologacao da API de taxas do PagBank
        /*
        if (response != null && response.paymentMethods() != null) {
            return switch (paymentMethod) {
                case CREDIT_CARD -> extractCreditCardFee(response, cardBrand);
                case PIX -> response.paymentMethods().pix() != null && response.paymentMethods().pix().amount() != null
                    ? response.paymentMethods().pix().amount().value().doubleValue()
                    : null;
                case BOLETO -> response.paymentMethods().boleto() != null && response.paymentMethods().boleto().amount() != null
                    ? response.paymentMethods().boleto().amount().value().doubleValue()
                    : null;
                default -> null;
            };
        }
        */

        return switch (paymentMethod) {
            case DEBIT_CARD -> donationValue * 0.0239;
            case CREDIT_CARD -> (donationValue * 0.0399) + 0.40;
            case PIX -> donationValue * 0.0189;
            case BOLETO -> 1.89;
        };
	}

	private Double extractCreditCardFee(ResponseCalculateFeesDto response, String cardBrand) {
		if (response.paymentMethods().creditCard() == null || cardBrand == null) {
			return null;
		}

		var creditCard = response.paymentMethods().creditCard();
		String normalizedBrand = cardBrand.toLowerCase();

		var brandData = creditCard.getBrand(normalizedBrand);

		if (brandData != null && brandData.installmentPlans() != null) {
			return extractFeeFromInstallmentPlan(brandData.installmentPlans());
		}

		return null;
	}

	private Double extractFeeFromInstallmentPlan(List<br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseCalculateFeesInstallmentPlan> installmentPlans) {
		if (installmentPlans == null || installmentPlans.isEmpty()) {
			return null;
		}

		var firstInstallment = installmentPlans.stream()
			.filter(plan -> plan.installments() != null && plan.installments() == 1)
			.findFirst()
			.orElse(installmentPlans.getFirst());

		if (firstInstallment == null || firstInstallment.amount() == null) {
			return null;
		}

		return firstInstallment.amount().value().doubleValue();
	}

    @Override
    public Double calculateFee(double value, PaymentMethodEnum paymentMethod, String cardBrand) {
        return extractFeeFromResponse(null, paymentMethod, cardBrand, value);
    }
}
