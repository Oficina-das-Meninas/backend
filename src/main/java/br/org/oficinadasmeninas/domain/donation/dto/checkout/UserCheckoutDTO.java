package br.org.oficinadasmeninas.domain.donation.dto.checkout;

public record UserCheckoutDTO(
        String name,
        String email,
        String tax_id,
        UserPhoneCheckoutDTO phone
) {}

