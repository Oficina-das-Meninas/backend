package br.org.oficinadasmeninas.infra.payment.mapper;

import br.org.oficinadasmeninas.domain.payment.Payment;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentDto;

public class PaymentMapper {

    public static PaymentDto toDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getDate(),
                payment.getStatus(),
                payment.getDonationId()
        );
    }
}
