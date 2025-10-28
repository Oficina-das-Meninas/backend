package br.org.oficinadasmeninas.infra.payment.service;

import br.org.oficinadasmeninas.domain.payment.Payment;
import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.dto.CreatePaymentDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentDto;
import br.org.oficinadasmeninas.domain.payment.repository.IPaymentRepository;
import br.org.oficinadasmeninas.domain.payment.service.IPaymentService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.infra.payment.mapper.PaymentMapper;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static br.org.oficinadasmeninas.infra.payment.mapper.PaymentMapper.toDto;

@Service
public class PaymentService implements IPaymentService {

    private final IPaymentRepository paymentRepository;

    public PaymentService(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentDto findById(UUID id) {
        var payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.PAYMENT_NOT_FOUND + id));

        return toDto(payment);
    }

    @Override
    public List<PaymentDto> findByDonationId(UUID donationId) {
        return paymentRepository.findByDonationId(donationId)
                .stream()
                .map(PaymentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public PaymentDto insert(CreatePaymentDto request) {
        var payment = new Payment(
                null,
                request.donationId(),
                request.gateway(),
                request.checkoutId(),
                request.method(),
                request.status()
        );

        paymentRepository.insert(payment);
        return toDto(payment);
    }

    @Override
    @Transactional
    public void updateStatus(UUID id, PaymentStatusEnum status) {

        var payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.PAYMENT_NOT_FOUND + id));

        payment.setStatus(status);
        paymentRepository.updateStatus(payment);
    }

    @Override
    public void updateMethod(UUID id, PaymentMethodEnum method) {
        var payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.PAYMENT_NOT_FOUND + id));

        payment.setMethod(method);
        paymentRepository.updateMethod(payment);
    }
}