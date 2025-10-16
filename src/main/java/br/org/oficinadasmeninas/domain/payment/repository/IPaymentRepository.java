package br.org.oficinadasmeninas.domain.payment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.payment.Payment;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;

public interface IPaymentRepository {
	
	Optional<Payment> findById(UUID id);
	
	List<Payment> findByDonation(UUID donationId);

	UUID create(Payment payment);
	
	void updateStatus(UUID id, PaymentStatusEnum status);
	
}
