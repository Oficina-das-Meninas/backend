package br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout;

import java.util.UUID;

public record ResponseCreateCheckoutDto(String link, String checkoutId, UUID referenceId) {}
