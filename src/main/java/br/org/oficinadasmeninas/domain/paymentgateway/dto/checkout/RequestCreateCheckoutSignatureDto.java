package br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout;

import java.util.Optional;

public record RequestCreateCheckoutSignatureDto(boolean isRecurrence, Optional<Integer> cycles ) {}
