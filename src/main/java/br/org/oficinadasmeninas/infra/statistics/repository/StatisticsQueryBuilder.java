package br.org.oficinadasmeninas.infra.statistics.repository;

public class StatisticsQueryBuilder {

    public static final String GET_INDICATORS = """
            SELECT ROUND(SUM(COALESCE(d.value_liquid, 0))::numeric, 2) AS total_donation_liquid
                  ,ROUND(SUM(d.value)::numeric, 2) AS total_donation
                  ,ROUND(AVG(NULLIF(d.value_liquid, 0))::numeric, 2) AS average_donation_liquid
                  ,ROUND(AVG(d.value)::numeric, 2) AS average_donation
                  ,COUNT(DISTINCT d.user_id) AS total_donors
                  ,(SELECT COUNT(*) FROM sponsorships WHERE is_active = true) AS active_sponsorships
            FROM donation d
            INNER JOIN payment p ON p.donation_id = d.id AND p.status = 'PAID'
            WHERE (?::timestamp IS NULL OR d.donation_at >= ?) 
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
            """;

    public static final String GET_DONATION_TYPE_DISTRIBUTION = """
            SELECT ROUND(SUM(CASE WHEN d.sponsorship_id IS NULL THEN COALESCE(d.value_liquid, 0) END)::numeric, 2) AS one_time_donation_liquid
                  ,ROUND(SUM(CASE WHEN d.sponsorship_id IS NULL THEN d.value END)::numeric, 2) AS one_time_donation
                  ,ROUND(SUM(CASE WHEN d.sponsorship_id IS NOT NULL THEN COALESCE(d.value_liquid, 0) END)::numeric, 2) AS recurring_donation_liquid
                  ,ROUND(SUM(CASE WHEN d.sponsorship_id IS NOT NULL THEN d.value END)::numeric, 2) AS recurring_donation
                  ,ROUND(SUM(COALESCE(d.value_liquid, 0))::numeric, 2) AS total_donation_liquid
                  ,ROUND(SUM(d.value)::numeric, 2) AS total_donation
            FROM donation d
            INNER JOIN payment p ON p.donation_id = d.id AND p.status = 'PAID'
            WHERE (?::timestamp IS NULL OR d.donation_at >= ?)
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
            """;

    public static final String GET_DONATIONS_BY_MONTH = """
            SELECT TO_CHAR(d.donation_at, 'YYYY-MM') AS period
                  ,CASE WHEN d.sponsorship_id IS NULL THEN 'ONE_TIME' ELSE 'RECURRING' END AS donation_type
                  ,SUM(COALESCE(d.value_liquid, 0)) AS total_value_liquid
                  ,SUM(d.value) AS total_value
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
                  ,SUM(COALESCE(d.value_liquid, 0)) AS total_value_liquid
                  ,SUM(d.value) AS total_value
            FROM donation d
            INNER JOIN payment p ON p.donation_id = d.id AND p.status = 'PAID'
            WHERE (?::timestamp IS NULL OR d.donation_at >= ?)
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
            GROUP BY TO_CHAR(d.donation_at, 'YYYY-MM-DD'), donation_type
            ORDER BY period;
            """;
}
