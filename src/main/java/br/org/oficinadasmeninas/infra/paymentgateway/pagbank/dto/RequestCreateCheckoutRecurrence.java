package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

public record RequestCreateCheckoutRecurrence(
        String name,
        RequestCreateCheckoutRecurrenceInterval interval,
        int billing_cycles
) {}

