package br.org.oficinadasmeninas.domain.payment.service;

import java.util.List;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.dto.CreatePaymentDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentDto;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.PaymentsMethodEnum;

public interface IPaymentService {
	
	PaymentDto getPaymentById(UUID id);
	
	List<PaymentDto> getPaymentsByDonation(UUID donationId);

	PaymentDto create(CreatePaymentDto payment);
	
	void updatePaymentStatus(UUID id, PaymentStatusEnum status);

    void updatePaymentMethod(UUID id, PaymentsMethodEnum status);

}
