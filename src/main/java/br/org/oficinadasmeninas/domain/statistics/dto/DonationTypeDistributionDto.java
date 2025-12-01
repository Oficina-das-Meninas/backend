package br.org.oficinadasmeninas.domain.statistics.dto;

import java.math.BigDecimal;

public record DonationTypeDistributionDto(
        BigDecimal oneTimeDonationLiquid,
        BigDecimal oneTimeDonation,
        BigDecimal recurringDonationLiquid,
        BigDecimal recurringDonation,
        BigDecimal totalDonationLiquid,
        BigDecimal totalDonation
) {
}
