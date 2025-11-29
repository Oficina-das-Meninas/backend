package br.org.oficinadasmeninas.domain.payment.dto;

import java.util.UUID;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;

public record CreatePaymentDto(
    PaymentStatusEnum status,
    UUID donationId
) {

}
