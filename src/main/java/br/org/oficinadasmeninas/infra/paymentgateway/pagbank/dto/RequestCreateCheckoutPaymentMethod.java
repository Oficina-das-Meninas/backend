package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

 import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;

public record RequestCreateCheckoutPaymentMethod(
        PaymentMethodEnum type
) {}
