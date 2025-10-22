package br.org.oficinadasmeninas.domain.payment.dto;

import br.org.oficinadasmeninas.domain.paymentgateway.dto.PaymentChargesDto;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseWebhookCustomer;

import java.util.List;
import java.util.UUID;

public record PaymentNotificationDto(String id, UUID reference_id, List<PaymentChargesDto> charges, ResponseWebhookCustomer customer) {

}
