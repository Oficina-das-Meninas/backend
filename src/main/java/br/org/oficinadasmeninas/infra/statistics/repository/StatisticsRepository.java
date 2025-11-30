package br.org.oficinadasmeninas.infra.statistics.repository;

import br.org.oficinadasmeninas.domain.statistics.dto.DonationsDto;
import br.org.oficinadasmeninas.domain.statistics.dto.IndicatorsStatisticsDto;
import br.org.oficinadasmeninas.domain.statistics.dto.DonationTypeDistributionDto;
import br.org.oficinadasmeninas.domain.statistics.repository.IStatisticsRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StatisticsRepository implements IStatisticsRepository {

    private final JdbcTemplate jdbc;

    public StatisticsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public IndicatorsStatisticsDto getIndicatorsByPeriod(LocalDate startDate, LocalDate endDate) {
        var result = jdbc.queryForObject(
                StatisticsQueryBuilder.GET_INDICATORS,
                this::mapRowIndicators,
                startDate != null ? startDate.atStartOfDay() : null,
                startDate != null ? startDate.atStartOfDay() : null,
                endDate != null ? endDate.atTime(23, 59, 59) : null,
                endDate != null ? endDate.atTime(23, 59, 59) : null
        );
        
        return new IndicatorsStatisticsDto(
                result != null ? result.totalDonations() : null,
                result != null ? result.averageDonationValue() : null,
                result != null ? result.totalDonors() : null,
                result != null ? result.activeSponsorships() : null
        );
    }

    @Override
    public DonationTypeDistributionDto getDonationTypeDistributionByPeriod(LocalDate startDate, LocalDate endDate) {
        return jdbc.queryForObject(
                StatisticsQueryBuilder.GET_DONATION_TYPE_DISTRIBUTION,
                this::mapRowDonationTypeDistribution,
                startDate != null ? startDate.atStartOfDay() : null,
                startDate != null ? startDate.atStartOfDay() : null,
                endDate != null ? endDate.atTime(23, 59, 59) : null,
                endDate != null ? endDate.atTime(23, 59, 59) : null
        );
    }

    @Override
    public DonationsDto getDonationsByPeriod(LocalDate startDate, LocalDate endDate, String groupBy) {
        String query = "day".equalsIgnoreCase(groupBy)
                ? StatisticsQueryBuilder.GET_DONATIONS_BY_DAY
                : StatisticsQueryBuilder.GET_DONATIONS_BY_MONTH;

        List<Map<String, Object>> results = jdbc.query(
                query,
                (rs, rowNum) -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("period", rs.getString("period"));
                    map.put("donation_type", rs.getString("donation_type"));
                    map.put("total_value", rs.getBigDecimal("total_value"));
                    return map;
                },
                startDate != null ? startDate.atStartOfDay() : null,
                startDate != null ? startDate.atStartOfDay() : null,
                endDate != null ? endDate.atTime(23, 59, 59) : null,
                endDate != null ? endDate.atTime(23, 59, 59) : null
        );

        List<DonationsDto.TimeSeriesDataPoint> oneTimeData = new ArrayList<>();
        List<DonationsDto.TimeSeriesDataPoint> recurringData = new ArrayList<>();

        for (Map<String, Object> row : results) {
            String period = (String) row.get("period");
            String donationType = (String) row.get("donation_type");
            BigDecimal value = (BigDecimal) row.get("total_value");

            DonationsDto.TimeSeriesDataPoint dataPoint =
                    new DonationsDto.TimeSeriesDataPoint(period, value);

            if ("ONE_TIME".equals(donationType)) {
                oneTimeData.add(dataPoint);
            } else {
                recurringData.add(dataPoint);
            }
        }

        return new DonationsDto(oneTimeData, recurringData);
    }

    private IndicatorsStatisticsDto mapRowIndicators(ResultSet rs, int rowNum) throws SQLException {
        return new IndicatorsStatisticsDto(
                rs.getBigDecimal("total_donation"),
                rs.getBigDecimal("average_donation_value"),
                rs.getLong("total_donors"),
                rs.getLong("active_sponsorships")
        );
    }

    private DonationTypeDistributionDto mapRowDonationTypeDistribution(ResultSet rs, int rowNum) throws SQLException {
        return new DonationTypeDistributionDto(
                rs.getBigDecimal("one_time_donation"),
                rs.getBigDecimal("recurring_donation"),
                rs.getBigDecimal("total_donation")
        );
    }
}
