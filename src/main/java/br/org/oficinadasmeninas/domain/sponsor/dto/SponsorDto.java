package br.org.oficinadasmeninas.domain.sponsor.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SponsorDto(
    double monthlyAmount,
    int billingDay,
    UUID userId,
    LocalDateTime sponsorSince,
    LocalDateTime sponsorUntil,
    Boolean isActive,
    String subscriptionId
) {}
