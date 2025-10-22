package br.org.oficinadasmeninas.infra.payment.service;

import java.util.List;
import java.util.UUID;

import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.PaymentsMethodEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.org.oficinadasmeninas.domain.payment.Payment;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.dto.CreatePaymentDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentDto;
import br.org.oficinadasmeninas.domain.payment.repository.IPaymentRepository;
import br.org.oficinadasmeninas.domain.payment.service.IPaymentService;
import br.org.oficinadasmeninas.infra.shared.exception.EntityNotFoundException;

@Service
public class PaymentService implements IPaymentService {

	private final IPaymentRepository paymentRepository;

	public PaymentService(IPaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}

	@Override
	public PaymentDto getPaymentById(UUID id) {
		Payment payment = paymentRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado com id: " + id));

		return new PaymentDto(payment.getId(), payment.getDonationId(), payment.getGateway(), payment.getCheckoutId(),
				payment.getMethod(), payment.getStatus());
	}

	@Override
	public List<PaymentDto> getPaymentsByDonation(UUID donationId) {
		return paymentRepository.findByDonationId(donationId)
                .stream()
                .map(payment -> new PaymentDto(
                        payment.getId(),
                        payment.getDonationId(),
                        payment.getGateway(),
                        payment.getCheckoutId(),
                        payment.getMethod(),
                        payment.getStatus()))
                .toList();
	}

	@Override
	@Transactional
	public PaymentDto create(CreatePaymentDto payment) {
	    Payment newPayment = new Payment(
                null,
                payment.donationId(),
                payment.gateway(),
                payment.checkoutId(),
                payment.method(),
                payment.status()
        );

        UUID id = paymentRepository.create(newPayment);

        return new PaymentDto(
                id,
                newPayment.getDonationId(),
                newPayment.getGateway(),
                newPayment.getCheckoutId(),
                newPayment.getMethod(),
                newPayment.getStatus());
	}

	@Override
	@Transactional
	public void updatePaymentStatus(UUID id, PaymentStatusEnum status) {
		 paymentRepository.updatePaymentStatus(id, status);
	}

    @Override
    public void updatePaymentMethod(UUID id, PaymentsMethodEnum status) {
        paymentRepository.updatePaymentMethod(id, status);
    }

}
