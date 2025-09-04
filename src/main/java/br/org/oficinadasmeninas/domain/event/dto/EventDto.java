package br.org.oficinadasmeninas.domain.event.dto;

import br.org.oficinadasmeninas.domain.event.Event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventDto(
        UUID id,
        String title,
        String previewImageUrl,
        String description,
        BigDecimal amount,
        LocalDateTime eventDate,
        String location,
        String urlToPlatform
) {

    public static EventDto fromEvent(Event event) {
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getPreviewImageUrl(),
                event.getDescription(),
                event.getAmount(),
                event.getEventDate(),
                event.getLocation(),
                event.getUrlToPlatform()
        );
    }
}