package br.org.oficinadasmeninas.domain.payment.dto;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;

public record PaymentChargesDto(String id, String reference_id, PaymentStatusEnum status) {

}
