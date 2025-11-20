package br.org.oficinadasmeninas.domain.sponsorship.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateSponsorshipDto(
    int billingDay,
    LocalDateTime startDate,
    Boolean isActive,
    String subscriptionId,
    UUID userId
) {}
