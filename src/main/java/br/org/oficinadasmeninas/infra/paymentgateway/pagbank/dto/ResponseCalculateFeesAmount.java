package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

public record ResponseCalculateFeesAmount(
    Integer value,
    Integer maxValue,
    Integer maxValueNoInterest
) {}
