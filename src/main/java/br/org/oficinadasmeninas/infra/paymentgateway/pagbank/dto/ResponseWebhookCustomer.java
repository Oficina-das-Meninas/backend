package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

public record ResponseWebhookCustomer(
        String name,
        String tax_id
) {}
