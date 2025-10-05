package br.org.oficinadasmeninas.domain.payment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.payment.Payment;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;

public interface IPaymentRepository {
	
	Optional<Payment> findPaymentById(UUID id);
	
	List<Payment> findPaymentsByDonation(UUID donationId);

	UUID createPayment(Payment payment);
	
	void updatePaymentStatus(UUID id, PaymentStatusEnum status);
	
}
