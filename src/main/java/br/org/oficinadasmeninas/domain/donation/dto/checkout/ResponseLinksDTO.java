package br.org.oficinadasmeninas.domain.donation.dto.checkout;

public record ResponseLinksDTO(
        String rel,
        String href,
        String method
){}
