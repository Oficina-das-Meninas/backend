package br.org.oficinadasmeninas.domain.paymentgateway.dto;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseWebhookCustomer;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseWebhookPaymentMethod;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseWebhookRecurring;

import java.util.Optional;

public record PaymentChargesDto(
        String id,
        String reference_id,
        PaymentStatusEnum status,
        ResponseWebhookPaymentMethod payment_method,
        Object recurring,
        ResponseWebhookCustomer customer
) {}
