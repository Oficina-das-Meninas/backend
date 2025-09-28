package br.org.oficinadasmeninas.infra.facade;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import br.org.oficinadasmeninas.domain.payment.dto.CheckoutDto;
import br.org.oficinadasmeninas.domain.payment.dto.CreateCheckoutDto;
import br.org.oficinadasmeninas.domain.payment.dto.LinkDto;
import br.org.oficinadasmeninas.domain.payment.dto.RequestCreateCheckoutDto;

@Service
public class PaymentGatewayFacade {

	private final WebClient webClient;

	@Value("${app.redirectCheckoutUrl}")
	private String redirectUrl;

	@Value("${app.notification_urls}")
	private String notificationUrls;

	@Value("${app.payment_notification_urls}")
	private String payment_notification_urls;
	
	@Value("${app.token}")
	private String token;

	public PaymentGatewayFacade(WebClient.Builder builder) {
		this.webClient = builder.baseUrl("https://sandbox.api.pagseguro.com")
				.build();
	}
	
	public CheckoutDto createCheckout(CreateCheckoutDto checkout) {
        try {
        	RequestCreateCheckoutDto request = new RequestCreateCheckoutDto(
	        			checkout.reference_id(),
	        			null,
	        			checkout.customer(),
	        			checkout.items(),
	        			null,
	        			redirectUrl, 
	        			List.of(notificationUrls), 
	        			List.of(payment_notification_urls)
        			);
        	
            var response = webClient.post()
                    .uri("/checkouts")
                    .header("Authorization", "Bearer " + token)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(CheckoutDto.class)
                    .block();

            Optional<LinkDto> payLink = response.links().stream()
                    .filter(link -> "PAY".equalsIgnoreCase(link.rel()))
                    .findFirst();

            return new CheckoutDto(
                    response.id(),
                    payLink.map(List::of).orElse(List.of()),
                    response.payment_methods(),
                    response.status()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
	}

}
