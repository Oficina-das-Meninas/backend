package br.org.oficinadasmeninas.domain.sponsorship.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateSponsorshipDto(
        UUID id,
        LocalDateTime cancelDate,
        Boolean isActive,
        String subscriptionId
) {}
