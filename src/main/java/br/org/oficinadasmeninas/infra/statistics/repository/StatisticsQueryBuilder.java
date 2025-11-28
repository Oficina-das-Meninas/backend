package br.org.oficinadasmeninas.infra.statistics.repository;

public class StatisticsQueryBuilder {

    public static final String GET_INDICATORS = """
            SELECT ROUND(SUM(COALESCE(d.value_liquid, COALESCE(d.value, 0)))::numeric, 2) as total_donation
                  ,ROUND(AVG(COALESCE(d.value_liquid, COALESCE(d.value, 0)))::numeric, 2) as average_donation_value
            	  ,COUNT(DISTINCT d.user_id) as total_donors
            	  ,(SELECT COUNT(*) FROM sponsorships WHERE is_active = true) as active_sponsorships
            FROM donation d
            WHERE (?::timestamp IS NULL OR d.donation_at >= ?)
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
            """;

    public static final String GET_DONATION_TYPE_DISTRIBUTION = """
            SELECT ROUND(COALESCE(SUM(CASE WHEN d.sponsorship_id IS NULL THEN COALESCE(d.value_liquid, COALESCE(d.value, 0)) ELSE 0 END), 0)::numeric, 2) as one_time_donation
                  ,ROUND(COALESCE(SUM(CASE WHEN d.sponsorship_id IS NOT NULL THEN COALESCE(d.value_liquid, COALESCE(d.value, 0)) ELSE 0 END), 0)::numeric, 2) as recurring_donation
                  ,ROUND(COALESCE(SUM(COALESCE(d.value_liquid, COALESCE(d.value, 0))), 0)::numeric, 2) as total_donation
            FROM donation d
            WHERE (?::timestamp IS NULL OR d.donation_at >= ?)
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
            """;

    public static final String GET_DONATIONS_BY_MONTH = """
            SELECT TO_CHAR(d.donation_at, 'YYYY-MM') as period
                  ,CASE WHEN d.sponsorship_id IS NULL THEN 'ONE_TIME' ELSE 'RECURRING' END as donation_type
                  ,COALESCE(SUM(COALESCE(d.value_liquid, COALESCE(d.value, 0))), 0) as total_value
            FROM donation d
            WHERE (?::timestamp IS NULL OR d.donation_at >= ?)
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
            GROUP BY TO_CHAR(d.donation_at, 'YYYY-MM'), donation_type
            ORDER BY period
            """;

    public static final String GET_DONATIONS_BY_DAY = """
            SELECT TO_CHAR(d.donation_at, 'YYYY-MM-DD') as period
                  ,CASE WHEN d.sponsorship_id IS NULL THEN 'ONE_TIME' ELSE 'RECURRING' END as donation_type
                  ,COALESCE(SUM(COALESCE(d.value_liquid, COALESCE(d.value, 0))), 0) as total_value
            FROM donation d
            WHERE (?::timestamp IS NULL OR d.donation_at >= ?)
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
            GROUP BY TO_CHAR(d.donation_at, 'YYYY-MM-DD'), donation_type
            ORDER BY period
            """;
}
