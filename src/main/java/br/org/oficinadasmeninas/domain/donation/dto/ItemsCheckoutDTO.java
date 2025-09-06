package br.org.oficinadasmeninas.domain.donation.dto;

public  record  ItemsCheckoutDTO(
        String name,
        Integer quantity,
        Long unit_amount,
        String imageURl
){}
