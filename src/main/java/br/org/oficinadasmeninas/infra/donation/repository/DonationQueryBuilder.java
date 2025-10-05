package br.org.oficinadasmeninas.infra.donation.repository;

public class DonationQueryBuilder {

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

}
