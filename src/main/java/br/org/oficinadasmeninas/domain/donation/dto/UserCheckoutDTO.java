package br.org.oficinadasmeninas.domain.donation.dto;

public record UserCheckoutDTO(
        String name,
        String email,
        String tax_id,
        UserPhoneCheckoutDTO phone
) {}

