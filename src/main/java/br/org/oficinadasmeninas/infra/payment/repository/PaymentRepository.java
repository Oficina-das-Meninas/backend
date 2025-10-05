package br.org.oficinadasmeninas.infra.payment.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.org.oficinadasmeninas.domain.payment.Payment;
import br.org.oficinadasmeninas.domain.payment.PaymentGatewayEnum;
import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.repository.IPaymentRepository;

@Repository
public class PaymentRepository implements IPaymentRepository {

	private final JdbcTemplate jdbc;

	public PaymentRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	public Optional<Payment> findPaymentById(UUID id) {
        try {
            Payment payment = jdbc.queryForObject(
            		PaymentQueryBuilder.SELECT_PAYMENT_BY_ID,
                    this::mapRowPayment,
                    id
            );
            return Optional.ofNullable(payment);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
	
	public List<Payment> findPaymentsByDonation(UUID donationId) {
        return jdbc.query(PaymentQueryBuilder.SELECT_PAYMENTS_BY_DONATION, this::mapRowPayment, donationId);
    }
	
	@Override
	public UUID createPayment(Payment payment) {
		 UUID id = payment.getId() != null ? payment.getId() : UUID.randomUUID();
	        jdbc.update(PaymentQueryBuilder.INSERT_PAYMENT,
	                id,
	                payment.getDonationId(),
	                payment.getGateway().name(),
	                payment.getCheckoutId(),
	                payment.getMethod() != null ? payment.getMethod().name() : null,
	                payment.getStatus().name()
	        );
	        return id;
	}

	@Override
	public void updatePaymentStatus(UUID id, PaymentStatusEnum status) {
		 jdbc.update(PaymentQueryBuilder.UPDATE_PAYMENT_STATUS, status.name(), id);
	}
	
	private Payment mapRowPayment(ResultSet rs, int rowNum) throws SQLException {
        Payment payment = new Payment(
                rs.getObject("id", UUID.class),
                rs.getObject("donation_id", UUID.class),
                PaymentGatewayEnum.valueOf(rs.getString("gateway")),
                rs.getString("checkout_id"),
                rs.getString("method") != null ? PaymentMethodEnum.valueOf(rs.getString("method")) : null,
                PaymentStatusEnum.valueOf(rs.getString("status"))
        );
        return payment;
    }

}
