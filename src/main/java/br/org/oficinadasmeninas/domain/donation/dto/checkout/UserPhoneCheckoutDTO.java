package br.org.oficinadasmeninas.domain.donation.dto.checkout;

public record UserPhoneCheckoutDTO(
        String country,
        String area,
        String number
){}
