package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

import java.util.List;

public record RequestCalculateFeesDto(
    List<String> paymentMethods,
    Integer value,
    Integer maxInstallments
) {}
