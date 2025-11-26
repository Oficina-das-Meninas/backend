package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

public record ResponseCalculateFeesDto(
    ResponseCalculateFeesPaymentMethods paymentMethods
) {}
