package br.org.oficinadasmeninas.domain.statistics.service;

import br.org.oficinadasmeninas.domain.statistics.dto.DonationsDto;
import br.org.oficinadasmeninas.domain.statistics.dto.DonationTypeDistributionDto;
import br.org.oficinadasmeninas.domain.statistics.dto.IndicatorsStatisticsDto;

import java.time.LocalDate;

public interface IStatisticsService {

    IndicatorsStatisticsDto getIndicatorsByPeriod(LocalDate startDate, LocalDate endDate);

    DonationTypeDistributionDto getDonationTypeDistributionByPeriod(LocalDate startDate, LocalDate endDate);

    DonationsDto getDonationsByPeriod(LocalDate startDate, LocalDate endDate, String groupBy);
}
