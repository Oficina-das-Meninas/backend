package br.org.oficinadasmeninas.infra.payment.repository;

public class PaymentQueryBuilder {

	public static final String INSERT_PAYMENT = """
			    INSERT INTO payment (id, donation_id, gateway, checkout_id, method, status)
			    VALUES (?, ?, ?, ?, ?, ?)
			""";

	public static final String SELECT_PAYMENT_BY_ID = """
			    SELECT id, donation_id, gateway, checkout_id, method, status
			    FROM payment
			    WHERE id = ?
			""";

	public static final String SELECT_PAYMENTS_BY_DONATION = """
			    SELECT id, donation_id, gateway, checkout_id, method, status
			    FROM payment
			    WHERE donation_id = ?
			""";

	public static final String UPDATE_PAYMENT_STATUS = """
			    UPDATE payment
			    SET status = ?
			    WHERE donation_id = ?
			""";

	public static final String UPDATE_PAYMENT_METHOD = """
			    UPDATE payment
			    SET method = ?
			    WHERE donation_id = ?
			""";

}
