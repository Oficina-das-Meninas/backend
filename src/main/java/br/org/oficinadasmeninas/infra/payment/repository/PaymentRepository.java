package br.org.oficinadasmeninas.infra.payment.repository;

import br.org.oficinadasmeninas.domain.payment.Payment;
import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.repository.IPaymentRepository;
import br.org.oficinadasmeninas.domain.paymentgateway.PaymentGatewayEnum;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
                payment.getDonationId(),
                payment.getGateway().name(),
                payment.getCheckoutId(),
                payment.getMethod() != null ? payment.getMethod().name() : null,
                payment.getStatus().name()
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

    @Override
    public Payment updateMethod(Payment payment) {

        jdbc.update(
                PaymentQueryBuilder.UPDATE_PAYMENT_METHOD,
                payment.getMethod().name(),
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

    private Payment mapRowPayment(ResultSet rs, int rowNum) throws SQLException {
        return new Payment(
                rs.getObject("id", UUID.class),
                rs.getObject("donation_id", UUID.class),
                PaymentGatewayEnum.valueOf(rs.getString("gateway")),
                rs.getString("checkout_id"),
                rs.getString("method") != null ? PaymentMethodEnum.valueOf(rs.getString("method")) : null,
                PaymentStatusEnum.valueOf(rs.getString("status"))
        );
    }
}
