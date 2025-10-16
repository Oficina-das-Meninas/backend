package br.org.oficinadasmeninas.domain.payment.dto;

import java.util.UUID;

import br.org.oficinadasmeninas.domain.payment.PaymentGatewayEnum;
import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;

public record CreatePaymentDto(UUID donationId, PaymentGatewayEnum gateway, String checkoutId,
		PaymentMethodEnum method, PaymentStatusEnum status) {

}
