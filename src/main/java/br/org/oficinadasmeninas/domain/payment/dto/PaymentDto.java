package br.org.oficinadasmeninas.domain.payment.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.paymentgateway.PaymentGatewayEnum;

public record PaymentDto(UUID id, UUID donationId, PaymentGatewayEnum gateway, String checkoutId, PaymentMethodEnum method,
                         PaymentStatusEnum status, LocalDateTime date ) {

}
