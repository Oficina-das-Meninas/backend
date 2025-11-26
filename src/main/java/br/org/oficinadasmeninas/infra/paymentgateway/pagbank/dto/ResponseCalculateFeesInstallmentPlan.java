package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

public record ResponseCalculateFeesInstallmentPlan(
    Integer installments,
    Integer installmentValue,
    Boolean interestFree,
    ResponseCalculateFeesAmount amount
) {}
