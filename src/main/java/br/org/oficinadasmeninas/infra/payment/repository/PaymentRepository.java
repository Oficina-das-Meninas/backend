package br.org.oficinadasmeninas.infra.payment.repository;

import br.org.oficinadasmeninas.domain.payment.Payment;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.repository.IPaymentRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PaymentRepository implements IPaymentRepository {

    private final JdbcTemplate jdbc;

    public PaymentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Payment insert(Payment payment) {

        var id = payment.getId() != null ? payment.getId() : UUID.randomUUID();
        payment.setId(id);

        jdbc.update(PaymentQueryBuilder.INSERT_PAYMENT,
                id,
                payment.getDate(),
                payment.getStatus().name(),
                payment.getDonationId()
        );

        return payment;
    }

    @Override
    public Payment updateStatus(Payment payment) {

        jdbc.update(
                PaymentQueryBuilder.UPDATE_PAYMENT_STATUS,
                payment.getStatus().name(),
                payment.getId()
        );

        return payment;
    }

    public Optional<Payment> findById(UUID id) {
        try {
            var payment = jdbc.queryForObject(
                    PaymentQueryBuilder.SELECT_PAYMENT_BY_ID,
                    this::mapRowPayment,
                    id
            );

            return Optional.ofNullable(payment);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Payment> findByDonationId(UUID donationId) {
        return jdbc.query(
                PaymentQueryBuilder.SELECT_PAYMENTS_BY_DONATION,
                this::mapRowPayment,
                donationId
        );
    }

    @Override
	public Payment updateDate(Payment payment) {
		jdbc.update(
                PaymentQueryBuilder.UPDATE_PAYMENT_DATE,
                payment.getDate(),
                payment.getId()
        );

        return payment;
	}

    private Payment mapRowPayment(ResultSet rs, int rowNum) throws SQLException {
        Timestamp ts = rs.getTimestamp("payment_date");

        return new Payment(
                rs.getObject("id", UUID.class),
                ts != null ? ts.toLocalDateTime() : null,
                PaymentStatusEnum.valueOf(rs.getString("status")),
                rs.getObject("donation_id", UUID.class)
        );
    }
}
