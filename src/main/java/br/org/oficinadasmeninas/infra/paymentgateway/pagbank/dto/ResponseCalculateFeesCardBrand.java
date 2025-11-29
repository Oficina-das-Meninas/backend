package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

import java.util.List;

public record ResponseCalculateFeesCardBrand(
    List<ResponseCalculateFeesInstallmentPlan> installmentPlans
) {}
