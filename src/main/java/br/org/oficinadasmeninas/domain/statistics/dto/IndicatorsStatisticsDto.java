package br.org.oficinadasmeninas.domain.statistics.dto;

import java.math.BigDecimal;

public record IndicatorsStatisticsDto(
        BigDecimal totalDonationLiquid,
        BigDecimal totalDonation,
        BigDecimal averageDonationLiquid,
        BigDecimal averageDonation,
        Long totalDonors,
        Long activeSponsorships
) {
}
