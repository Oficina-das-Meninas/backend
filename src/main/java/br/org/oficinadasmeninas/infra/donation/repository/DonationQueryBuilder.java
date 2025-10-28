
package br.org.oficinadasmeninas.infra.donation.repository;

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
               (CASE WHEN EXISTS (
                       SELECT 1
                       FROM sponsors s
                       WHERE s.userid = d.user_id
               ) THEN 'RECURRING'
               ELSE 'ONE_TIME'
               END) = ?)
        """;

    public static final String INSERT_DONATION = """
			    INSERT INTO donation (id, value, donation_at, user_id, status)
			    VALUES (?, ?, ?, ?, ?)
			""";

	public static final String SELECT_DONATION_BY_ID = """
			    SELECT id, value, donation_at, user_id, status
			    FROM donation
			    WHERE id = ?
			""";
	
	public static final String SELECT_DONATION_BY_USER_ID = """
		    SELECT id, value, donation_at, user_id, status
		    FROM donation
		    WHERE user_id = ?
		""";

	public static final String SELECT_ALL_DONATIONS = """
			    SELECT id, value, donation_at, user_id, status
			    FROM donation
			""";

	public static final String UPDATE_DONATION_STATUS = """
			    UPDATE donation
			    SET status = ?
			    WHERE id = ?
			""";


    public static final String GET_FILTERED_DONATIONS = """
            SELECT d.id
                  ,d.value
                  ,d.donation_at
                  ,d.user_id
                  ,u.name AS donor_name
                  ,d.status
                  ,CASE WHEN EXISTS (
                           SELECT 1
                           FROM sponsors s
                           WHERE s.userid = d.user_id
                       ) THEN 'RECURRING'
                   ELSE 'ONE_TIME'
                   END AS donation_type
                  ,CASE WHEN EXISTS (
                        SELECT 1 FROM sponsors s WHERE s.userid = d.user_id AND s.isactive = TRUE
                   ) THEN 'ACTIVE'
                        WHEN EXISTS (
                        SELECT 1 FROM sponsors s WHERE s.userid = d.user_id AND s.isactive = FALSE
                   ) THEN 'INACTIVE'
                   ELSE null
                   END AS sponsor_status
            FROM donation d
            LEFT JOIN users u ON d.user_id = u.id
            WHERE (?::text IS NULL OR u.name ILIKE '%' || ? || '%')
              AND (?::timestamp IS NULL OR d.donation_at >= ?)
              AND (?::timestamp IS NULL OR d.donation_at <= ?)
              AND (?::text IS NULL OR d.status = ?)
              AND (?::text IS NULL OR
                   (CASE WHEN EXISTS (
                           SELECT 1
                           FROM sponsors s
                           WHERE s.userid = d.user_id
                   ) THEN 'RECURRING'
                   ELSE 'ONE_TIME'
                   END) = ?)
            ORDER BY d.donation_at DESC
            LIMIT ? OFFSET ?;
    """;
}