package br.org.oficinadasmeninas.domain.donation.dto.checkout;

public  record  ItemsCheckoutDTO(
        String name,
        Integer quantity,
        Long unit_amount,
        String imageURl
){}
