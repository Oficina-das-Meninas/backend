package br.org.oficinadasmeninas.domain.payment.service;

import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.dto.CreatePaymentDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IPaymentService {
	
	PaymentDto insert(CreatePaymentDto payment);

	void updateStatus(UUID id, PaymentStatusEnum status);

    void updateMethod(UUID id, PaymentMethodEnum method);
    
    void updatePaymentDate(UUID id, LocalDateTime date);

	List<PaymentDto> findByDonationId(UUID donationId);

	PaymentDto findById(UUID id);
}
