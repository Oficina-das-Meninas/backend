package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.PaymentsMethodEnum;

public record ResponseWebhookPaymentMethod(
        PaymentsMethodEnum type
) {}
