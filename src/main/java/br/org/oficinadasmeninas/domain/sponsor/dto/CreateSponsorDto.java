package br.org.oficinadasmeninas.domain.sponsor.dto;

import java.util.UUID;

public record CreateSponsorDto(long monthlyAmount, int billingDay, String subscriptionId, UUID userId) {}
