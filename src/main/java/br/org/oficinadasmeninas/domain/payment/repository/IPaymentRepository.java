package br.org.oficinadasmeninas.domain.payment.repository;

import br.org.oficinadasmeninas.domain.payment.Payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface IPaymentRepository {

    Optional<Payment> findById(UUID id);

    List<Payment> findByDonationId(UUID donationId);

    Payment insert(Payment payment);

    Payment updateStatus(Payment payment);

    void cancelPaymentByDonationId(UUID donationId);

    Payment updateDate(Payment payment);
}
