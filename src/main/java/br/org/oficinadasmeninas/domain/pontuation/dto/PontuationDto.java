package br.org.oficinadasmeninas.domain.pontuation.dto;

import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.pontuation.Bonuses;

import java.time.LocalDateTime;
import java.util.UUID;

public record PontuationDto(
    UUID id,
    Long donatedValue,
    LocalDateTime donatedDate,
    Long earnedPoints,
    Long totalPoints,
    PaymentMethodEnum paymentMethod,
    Bonuses bonuses,
    int recurrenceSequence
) { }