package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

public record RequestCreateCheckoutItem(String name, Integer quantity, Long unit_amount, String image_url) {}
