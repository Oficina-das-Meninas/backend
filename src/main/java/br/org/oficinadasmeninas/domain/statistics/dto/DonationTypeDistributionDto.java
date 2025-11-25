package br.org.oficinadasmeninas.domain.statistics.dto;

import java.math.BigDecimal;

public record DonationTypeDistributionDto(
        BigDecimal oneTimeDonation,
        BigDecimal recurringDonation,
        BigDecimal totalDonations
) {
}
