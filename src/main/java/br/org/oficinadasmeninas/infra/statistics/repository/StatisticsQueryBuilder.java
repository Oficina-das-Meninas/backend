package br.org.oficinadasmeninas.infra.statistics.repository;

public class StatisticsQueryBuilder {

    public static final String GET_INDICATORS = """
        WITH filtered_donations AS (
            SELECT d.value_liquid
                  ,d.value
                  ,d.user_id
            FROM donation d
            INNER JOIN payment p ON p.donation_id = d.id AND p.status = 'PAID'
            WHERE (?::timestamp IS NULL OR d.donation_at >= ?)
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
        ),
        active_sponsorships AS (
            SELECT 1
            FROM sponsorships s
            WHERE s.is_active = TRUE
              AND ( ?::timestamp IS NULL OR s.start_date <= ? )
              AND ( ?::timestamp IS NULL OR s.cancel_date IS NULL OR s.cancel_date >= ? )
        )
        SELECT COALESCE(ROUND(SUM(COALESCE(fd.value_liquid, 0))::numeric, 2), 0) AS total_donation_liquid
              ,COALESCE(ROUND(SUM(fd.value)::numeric, 2), 0) AS total_donation
              ,COALESCE(ROUND(AVG(NULLIF(fd.value_liquid, 0))::numeric, 2), 0) AS average_donation_liquid
              ,COALESCE(ROUND(AVG(fd.value)::numeric, 2), 0) AS average_donation
              ,COALESCE(COUNT(DISTINCT fd.user_id), 0) AS total_donors
              ,COALESCE((SELECT COUNT(*) FROM active_sponsorships), 0) AS active_sponsorships
        FROM filtered_donations fd
        """;

    public static final String GET_DONATION_TYPE_DISTRIBUTION = """
            SELECT COALESCE(ROUND(SUM(CASE WHEN d.sponsorship_id IS NULL THEN COALESCE(d.value_liquid, 0) ELSE 0 END)::numeric, 2), 0) AS one_time_donation_liquid
                  ,COALESCE(ROUND(SUM(CASE WHEN d.sponsorship_id IS NULL THEN d.value ELSE 0 END)::numeric, 2), 0) AS one_time_donation
                  ,COALESCE(ROUND(SUM(CASE WHEN d.sponsorship_id IS NOT NULL THEN COALESCE(d.value_liquid, 0) ELSE 0 END)::numeric, 2), 0) AS recurring_donation_liquid
                  ,COALESCE(ROUND(SUM(CASE WHEN d.sponsorship_id IS NOT NULL THEN d.value ELSE 0 END)::numeric, 2), 0) AS recurring_donation
                  ,COALESCE(ROUND(SUM(COALESCE(d.value_liquid, 0))::numeric, 2), 0) AS total_donation_liquid
                  ,COALESCE(ROUND(SUM(d.value)::numeric, 2), 0) AS total_donation
            FROM donation d
            INNER JOIN payment p ON p.donation_id = d.id AND p.status = 'PAID'
            WHERE (?::timestamp IS NULL OR d.donation_at >= ?)
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
            """;

    public static final String GET_DONATIONS_BY_MONTH = """
            SELECT TO_CHAR(d.donation_at, 'YYYY-MM') AS period
                  ,CASE WHEN d.sponsorship_id IS NULL THEN 'ONE_TIME' ELSE 'RECURRING' END AS donation_type
                  ,COALESCE(SUM(COALESCE(d.value_liquid, 0)), 0) AS total_value_liquid
                  ,COALESCE(SUM(d.value), 0) AS total_value
            FROM donation d
            INNER JOIN payment p ON p.donation_id = d.id AND p.status = 'PAID'
            WHERE (?::timestamp IS NULL OR d.donation_at >= ?)
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
            GROUP BY TO_CHAR(d.donation_at, 'YYYY-MM'), donation_type
            ORDER BY period;
            """;

    public static final String GET_DONATIONS_BY_DAY = """
            SELECT TO_CHAR(d.donation_at, 'YYYY-MM-DD') AS period
                  ,CASE WHEN d.sponsorship_id IS NULL THEN 'ONE_TIME' ELSE 'RECURRING' END AS donation_type
                  ,COALESCE(SUM(COALESCE(d.value_liquid, 0)), 0) AS total_value_liquid
                  ,COALESCE(SUM(d.value), 0) AS total_value
            FROM donation d
            INNER JOIN payment p ON p.donation_id = d.id AND p.status = 'PAID'
            WHERE (?::timestamp IS NULL OR d.donation_at >= ?)
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
            GROUP BY TO_CHAR(d.donation_at, 'YYYY-MM-DD'), donation_type
            ORDER BY period
            """;
}
