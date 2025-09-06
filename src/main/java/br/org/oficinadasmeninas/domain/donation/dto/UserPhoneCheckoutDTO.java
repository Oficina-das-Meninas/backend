package br.org.oficinadasmeninas.domain.donation.dto;

public record UserPhoneCheckoutDTO(
        String country,
        String area,
        String number
){}
