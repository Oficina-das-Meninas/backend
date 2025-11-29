package br.org.oficinadasmeninas.domain.statistics.dto;

import java.math.BigDecimal;

public record IndicatorsStatisticsDto(
        BigDecimal totalDonations,
        BigDecimal averageDonationValue,
        Long totalDonors,
        Long activeSponsorships
) {
}
