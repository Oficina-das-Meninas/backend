package br.org.oficinadasmeninas.domain.donation.dto;

public record ResponseLinksDTO(
        String rel,
        String href,
        String method
){}
