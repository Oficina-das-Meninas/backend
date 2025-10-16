package br.org.oficinadasmeninas.domain.payment.dto;

import java.util.List;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;

public record CheckoutDto(String id, List<LinkDto> links, List<PaymentMethodDto> payment_methods, PaymentStatusEnum status) {

}
