package br.org.oficinadasmeninas.domain.statistics.repository;

import br.org.oficinadasmeninas.domain.statistics.dto.DonationsDto;
import br.org.oficinadasmeninas.domain.statistics.dto.IndicatorsStatisticsDto;
import br.org.oficinadasmeninas.domain.statistics.dto.DonationTypeDistributionDto;

import java.time.LocalDate;

public interface IStatisticsRepository {

    IndicatorsStatisticsDto getIndicatorsByPeriod(LocalDate startDate, LocalDate endDate);

    DonationTypeDistributionDto getDonationTypeDistributionByPeriod(LocalDate startDate, LocalDate endDate);

    DonationsDto getDonationsByPeriod(LocalDate startDate, LocalDate endDate, String groupBy);
}
