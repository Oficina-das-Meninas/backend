package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.PaymentsMethodEnum;

public record RequestCreateCheckoutPaymentMethod(
        PaymentsMethodEnum type
) {}
