package br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout;

public record RequestCreateCheckoutCustomerDto(String name, String phone, String email, String document) {}
