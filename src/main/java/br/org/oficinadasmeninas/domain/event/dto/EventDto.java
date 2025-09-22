package br.org.oficinadasmeninas.domain.event.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventDto(
        UUID id,
        String title,
        String previewImageUrl,
        String partnersImageUrl,
        String description,
        BigDecimal amount,
        LocalDateTime eventDate,
        String location,
        String urlToPlatform
) { }