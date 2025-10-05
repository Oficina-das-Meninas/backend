package br.org.oficinadasmeninas.domain.payment.dto;

import java.util.List;
import java.util.UUID;

public record PaymentNotificationDto(String id, UUID reference_id, List<PaymentChargesDto> charges) {

}
