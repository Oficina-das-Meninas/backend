package br.org.oficinadasmeninas.domain.sponsor.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateSponsorDto(
        UUID id,
        LocalDateTime sponsorUntil,
        Boolean isActive,
        String subscriptionId
) {}
