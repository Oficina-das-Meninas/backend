package br.org.oficinadasmeninas.domain.payment.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;

public record PaymentDto(
    UUID id,
    LocalDateTime date,
    PaymentStatusEnum status,
    UUID donationId
) {

}
