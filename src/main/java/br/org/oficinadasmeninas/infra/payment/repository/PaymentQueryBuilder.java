package br.org.oficinadasmeninas.infra.payment.repository;

public class PaymentQueryBuilder {

	public static final String INSERT_PAYMENT = """
			    INSERT INTO payment (id, payment_date, status, donation_id)
			    VALUES (?, ?, ?, ?)
			""";

	public static final String SELECT_PAYMENT_BY_ID = """
			    SELECT id, payment_date, status, donation_id
			    FROM payment
			    WHERE id = ?
			""";

	public static final String SELECT_PAYMENTS_BY_DONATION = """
			    SELECT id, payment_date, status, donation_id
			    FROM payment
			    WHERE donation_id = ?
			""";

	public static final String UPDATE_PAYMENT_STATUS = """
			    UPDATE payment
			    SET status = ?
			    WHERE id = ?
			""";

	public static final String UPDATE_PAYMENT_DATE = """
		    UPDATE payment
		    SET payment_date = ?
		    WHERE id = ?
		""";
}
