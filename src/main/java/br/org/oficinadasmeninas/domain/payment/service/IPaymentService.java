package br.org.oficinadasmeninas.domain.payment.service;

import java.util.List;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.dto.CreatePaymentDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentDto;

public interface IPaymentService {
	
	PaymentDto getPaymentById(UUID id);
	
	List<PaymentDto> getPaymentsByDonation(UUID donationId);

	PaymentDto createPayment(CreatePaymentDto payment);
	
	void updatePaymentStatus(UUID id, PaymentStatusEnum status);
	
}
