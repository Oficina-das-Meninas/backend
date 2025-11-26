package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

public record ResponseCalculateFeesPaymentMethods(
    ResponseCalculateFeesCreditCard creditCard,
    ResponseCalculateFeesPix pix,
    ResponseCalculateFeesBoleto boleto
) {}
