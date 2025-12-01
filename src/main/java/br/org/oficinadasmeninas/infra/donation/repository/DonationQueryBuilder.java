
package br.org.oficinadasmeninas.infra.donation.repository;

import java.util.Map;

public class DonationQueryBuilder {

    public static final String SELECT_COUNT = """
        SELECT count(*)
        FROM donation d
        LEFT JOIN users u        ON d.user_id = u.id
        LEFT JOIN account a      ON a.id = u.account_id
        LEFT JOIN sponsorships s ON d.sponsorship_id = s.id
        LEFT JOIN payment p      ON d.id = p.donation_id
        WHERE (?::text IS NULL OR a.name ILIKE '%' || ? || '%')
          AND (?::timestamp IS NULL OR d.donation_at >= ?)
          AND (?::timestamp IS NULL OR d.donation_at <= ?)
          AND (?::text IS NULL OR p.status = ?)
          AND (?::text IS NULL OR
               (CASE WHEN d.sponsorship_id IS NOT NULL THEN 'RECURRING'
               ELSE 'ONE_TIME'
               END) = ?)
        """;

    public static final String INSERT_DONATION = """
			    INSERT INTO donation (id, value, checkout_id, gateway, sponsorship_id, method, user_id, donation_at)
			    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
			""";

	public static final String SELECT_DONATION_BY_ID = """
			    SELECT id, value, checkout_id, gateway, sponsorship_id, method, user_id, donation_at
			    FROM donation
			    WHERE id = ?
			""";
	
	public static final String SELECT_DONATION_BY_USER_ID = """
		    SELECT id, value, checkout_id, gateway, sponsorship_id, method, user_id, donation_at
		    FROM donation
		    WHERE user_id = ?
		""";

	public static final String SELECT_ALL_DONATIONS = """
			    SELECT id, value, checkout_id, gateway, sponsorship_id, method, user_id, donation_at
			    FROM donation
			""";


	public static final String UPDATE_DONATION_METHOD = """
			    UPDATE donation
			    SET method = ?
			    WHERE id = ?
			""";

	public static final String UPDATE_FEE_AND_LIQUID_VALUE = """
			    UPDATE donation
			    SET fee = ?, value_liquid = ?
			    WHERE id = ?
			""";

	public static final String UPDATE_CARD_BRAND = """
			    UPDATE donation
			    SET card_brand = ?
			    WHERE id = ?
			""";

    public static final Map<String, String> ALLOWED_SORT_FIELDS = Map.of(
            "donorName", "a.name",
            "value", "d.value",
            "valueLiquid", "d.value_liquid",
            "donationAt", "d.donation_at",
            "status", "p.status",
            "donationType", "(CASE WHEN d.sponsorship_id IS NOT NULL THEN 'RECURRING' ELSE 'ONE_TIME' END)",
            "sponsorStatus", "(CASE WHEN s.is_active = TRUE THEN 'ACTIVE' WHEN s.is_active = FALSE THEN 'INACTIVE' ELSE null END)"
    );

    public static final String GET_FILTERED_DONATIONS = """
            SELECT d.id
            	  ,d.value
            	  ,d.value_liquid
            	  ,d.donation_at
            	  ,d.user_id
            	  ,u.account_id
            	  ,a.name AS donor_name
            	  ,CASE WHEN d.sponsorship_id IS NOT NULL THEN 'RECURRING'
            	   ELSE 'ONE_TIME'
            	   END AS donation_type 
            	  ,CASE WHEN s.is_active = TRUE THEN 'ACTIVE'
            			WHEN s.is_active = FALSE THEN 'INACTIVE'
            	   ELSE null
            	   END AS sponsor_status
            	  ,p.status
            FROM donation d
            LEFT JOIN users u        ON d.user_id = u.id
            LEFT JOIN account a      ON a.id = u.account_id
            LEFT JOIN sponsorships s ON d.sponsorship_id = s.id
            LEFT JOIN payment p      ON d.id = p.donation_id
            WHERE (?::text IS NULL OR a.name ILIKE '%' || ? || '%')
              AND (?::timestamp IS NULL OR d.donation_at >= ?)
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
              AND (?::text IS NULL OR p.status = ?)
              AND (?::text IS NULL OR
            	   (CASE WHEN d.sponsorship_id IS NOT NULL THEN 'RECURRING'
            	   ELSE 'ONE_TIME'
            	   END) = ?)
            %ORDER_BY%
            LIMIT ? OFFSET ?
    """;

    public static final String SELECT_PENDING_CHECKOUTS_BY_USER_ID = """
            SELECT
                    d.id,
                    d.donation_at,
                    d.checkout_id,
                    d.value,
                    d.user_id,
                    d.gateway,
                    d.sponsorship_id,
                    d.method,
                    p.status
            FROM donation d
            LEFT JOIN payment p
            ON d.id = p.donation_id
            WHERE
                d.user_id = ?
                AND p.status = 'WAITING'
                AND d.checkout_id IS NOT null;
        """;
}