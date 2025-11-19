
package br.org.oficinadasmeninas.infra.donation.repository;

import java.util.Map;

public class DonationQueryBuilder {

    public static final String SELECT_COUNT = """
        SELECT count(*)
        FROM donation d
        LEFT JOIN users u ON d.user_id = u.id
        WHERE (?::text IS NULL OR u.name ILIKE '%' || ? || '%')
          AND (?::timestamp IS NULL OR d.donation_at >= ?)
          AND (?::timestamp IS NULL OR d.donation_at <= ?)
          AND (?::text IS NULL OR d.status = ?)
          AND (?::text IS NULL OR
               (CASE WHEN d.sponsor_id IS NOT NULL THEN 'RECURRING'
               ELSE 'ONE_TIME'
               END) = ?)
        """;

    public static final String INSERT_DONATION = """
			    INSERT INTO donation (id, value, checkout_id, gateway, sponsor_id, method, user_id, donation_at)
			    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
			""";

	public static final String SELECT_DONATION_BY_ID = """
			    SELECT id, value, checkout_id, gateway, sponsor_id, method, user_id, donation_at
			    FROM donation
			    WHERE id = ?
			""";
	
	public static final String SELECT_DONATION_BY_USER_ID = """
		    SELECT id, value, checkout_id, gateway, sponsor_id, method, user_id, donation_at
		    FROM donation
		    WHERE user_id = ?
		""";

	public static final String SELECT_ALL_DONATIONS = """
			    SELECT id, value, checkout_id, gateway, sponsor_id, method, user_id, donation_at
			    FROM donation
			""";


	public static final String UPDATE_DONATION_METHOD = """
			    UPDATE donation
			    SET method = ?
			    WHERE id = ?
			""";

    public static final Map<String, String> ALLOWED_SORT_FIELDS = Map.of(
            "donorName", "u.name",
            "value", "d.value",
            "donationAt", "d.donation_at",
            "status", "d.status",
            "donationType", "(CASE WHEN d.sponsor_id IS NOT NULL THEN 'RECURRING' ELSE 'ONE_TIME' END)",
            "sponsorStatus", "(CASE WHEN s.isactive = TRUE THEN 'ACTIVE' WHEN s.isactive = FALSE THEN 'INACTIVE' ELSE null END)"
    );

    public static final String GET_FILTERED_DONATIONS = """
            SELECT d.id
                  ,d.value
                  ,d.donation_at
                  ,d.user_id
                  ,u.name AS donor_name
                  ,d.status
                  ,CASE WHEN d.sponsor_id IS NOT NULL THEN 'RECURRING'
                   ELSE 'ONE_TIME'
                   END AS donation_type
                  ,CASE WHEN s.isactive = TRUE THEN 'ACTIVE'
                        WHEN s.isactive = FALSE THEN 'INACTIVE'
                   ELSE null
                   END AS sponsor_status
            FROM donation d
            LEFT JOIN users u ON d.user_id = u.id
            LEFT JOIN sponsorships s ON d.sponsor_id = s.id
            WHERE (?::text IS NULL OR u.name ILIKE '%' || ? || '%')
              AND (?::timestamp IS NULL OR d.donation_at >= ?)
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
              AND (?::text IS NULL OR d.status = ?)
              AND (?::text IS NULL OR
                   (CASE WHEN d.sponsor_id IS NOT NULL THEN 'RECURRING'
                   ELSE 'ONE_TIME'
                   END) = ?)
            %ORDER_BY%
            LIMIT ? OFFSET ?;
    """;
}