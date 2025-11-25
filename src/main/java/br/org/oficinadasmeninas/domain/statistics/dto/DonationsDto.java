package br.org.oficinadasmeninas.domain.statistics.dto;

import java.math.BigDecimal;
import java.util.List;

public record DonationsDto(
        List<TimeSeriesDataPoint> oneTimeDonations,
        List<TimeSeriesDataPoint> recurringDonations
) {
    public record TimeSeriesDataPoint(
            String period,
            BigDecimal value
    ) {
    }
}
