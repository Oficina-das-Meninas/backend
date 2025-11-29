package br.org.oficinadasmeninas.infra.statistics.service;

import br.org.oficinadasmeninas.domain.statistics.dto.DonationsDto;
import br.org.oficinadasmeninas.domain.statistics.dto.DonationTypeDistributionDto;
import br.org.oficinadasmeninas.domain.statistics.dto.IndicatorsStatisticsDto;
import br.org.oficinadasmeninas.domain.statistics.repository.IStatisticsRepository;
import br.org.oficinadasmeninas.domain.statistics.service.IStatisticsService;
import br.org.oficinadasmeninas.infra.shared.exception.InvalidDateRangeException;
import br.org.oficinadasmeninas.infra.shared.exception.RequiredDateRangeException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StatisticsService implements IStatisticsService {

    private final IStatisticsRepository statisticsRepository;

    public StatisticsService(IStatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public IndicatorsStatisticsDto getIndicatorsByPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new RequiredDateRangeException();
        }

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException();
        }

        var indicators = statisticsRepository.getIndicatorsByPeriod(startDate, endDate);

        return new IndicatorsStatisticsDto(
                indicators.totalDonations(),
                indicators.averageDonationValue(),
                indicators.totalDonors(),
                indicators.activeSponsorships()
        );
    }

    @Override
    public DonationTypeDistributionDto getDonationTypeDistributionByPeriod(LocalDate startDate, LocalDate endDate){
        var donorTypeDistribution = statisticsRepository.getDonationTypeDistributionByPeriod(startDate, endDate);
        return new DonationTypeDistributionDto(
                donorTypeDistribution.oneTimeDonation(),
                donorTypeDistribution.recurringDonation(),
                donorTypeDistribution.totalDonations()
        );
    }

    @Override
    public DonationsDto getDonationsByPeriod(LocalDate startDate, LocalDate endDate, String groupBy) {
        if (startDate == null || endDate == null) {
            throw new RequiredDateRangeException();
        }

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException();
        }

        return statisticsRepository.getDonationsByPeriod(startDate, endDate, groupBy);
    }
}
