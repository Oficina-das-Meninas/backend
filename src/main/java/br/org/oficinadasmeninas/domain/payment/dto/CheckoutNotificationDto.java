package br.org.oficinadasmeninas.domain.payment.dto;

import java.util.UUID;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;

public record CheckoutNotificationDto(String id, UUID reference_id, PaymentStatusEnum status) {

}
