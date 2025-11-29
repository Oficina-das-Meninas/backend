package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

public record RequestCreateCheckoutRecurrenceInterval(
    String unit,
    int lenght
) {}
