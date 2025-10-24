package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

public record RequestCreateCheckoutCustomerPhone(String country, String area, String number) {}
