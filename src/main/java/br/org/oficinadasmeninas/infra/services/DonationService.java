package br.org.oficinadasmeninas.infra.services;

import br.org.oficinadasmeninas.domain.donation.dto.checkout.RequestCheckoutDTO;
import br.org.oficinadasmeninas.domain.donation.dto.checkout.ResponseCreateCheckoutDTO;
import br.org.oficinadasmeninas.domain.donation.dto.checkout.ResponseLinksDTO;
import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class DonationService implements IDonationService {
    private final WebClient webClient;

    public DonationService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://sandbox.api.pagseguro.com")
                .defaultHeader("Authorization", "Bearer ")
                .build();
    }

    public ResponseCreateCheckoutDTO createCheckout(RequestCheckoutDTO request) {
        // User, valor(itens_checkout), ID da doação (reference_id)

        try {
            var response = webClient.post()
                    .uri("/checkouts")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ResponseCreateCheckoutDTO.class)
                    .block();

            Optional<ResponseLinksDTO> payLink = response.links().stream()
                    .filter(link -> "PAY".equalsIgnoreCase(link.rel()))
                    .findFirst();

            return new ResponseCreateCheckoutDTO(
                    response.id(),
                    payLink.map(List::of).orElse(List.of()),
                    response.status()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
