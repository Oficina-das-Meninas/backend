package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.service;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.ResponseCreateCheckoutDto;
import br.org.oficinadasmeninas.domain.paymentgateway.service.IPaymentGatewayService;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.PaymentsMethodEnum;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.*;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.mappers.RequestCreateCheckoutPagbankMapper;
import br.org.oficinadasmeninas.infra.shared.exception.PaymentGatewayException;
import br.org.oficinadasmeninas.presentation.shared.utils.IsoDateFormater;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentGatewayService implements IPaymentGatewayService {
    private final RequestCreateCheckoutPagbankMapper mapper;

    private final WebClient webClient;

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



    public PaymentGatewayService(RequestCreateCheckoutPagbankMapper mapper, WebClient.Builder builder, @Value("${app.url}") String url) {
        this.mapper = mapper;
        this.webClient = builder.baseUrl(url)
                .build();
    }

    @Override
    public ResponseCreateCheckoutDto createCheckout(RequestCreateCheckoutDto requestCreateCheckoutDto) {



        // Default configurations Pagbank

        RequestCreateCheckoutConfig defaults = new RequestCreateCheckoutConfig(
                IsoDateFormater.addHours(4),
                redirectUrl,
                requestCreateCheckoutDto.signatureDto().isRecurrence()? "APADRINHAMENTO OFICINA DAS MENINAS": "DOAÇÃO OFICINA DAS MENINAS",
                1,
                List.of(notificationUrls),
                List.of(paymentNotificationUrls),
                List.of(PaymentsMethodEnum.CREDIT_CARD),
                "https://dev.apollomusic.com.br/logo_preenchimento_branco.png",
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
    public void updatePaymentStatus(UUID paymentId, PaymentStatusEnum paymentStatus) {

    }

    @Override
    public void updateCheckoutStatus(String checkoutId, UUID paymentId) {

    }

    private ResponseCreateCheckoutPagbank createPagBankCheckout(RequestCreateCheckoutPagbank checkoutPagbank) {
        try {
            System.out.println(checkoutPagbank);
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
                       // captura o body da resposta do erro
                System.err.println("Status code: " + e.getRawStatusCode());
                System.err.println("Response body: " + e.getResponseBodyAsString());
                System.err.println(checkoutPagbank.toString());
                throw new PaymentGatewayException(e.getRawStatusCode() + " " + e.getStatusText());

        }
    }
}
