package br.org.oficinadasmeninas.infra.services;

import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DonationService implements IDonationService {
    private final WebClient webClient;

    public DonationService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://sandbox.api.pagseguro.com")
                .defaultHeader("Authorization", "Bearer ")
                .build();
    }


}
