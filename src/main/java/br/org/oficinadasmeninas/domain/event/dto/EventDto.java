package br.org.oficinadasmeninas.domain.event.dto;

import br.org.oficinadasmeninas.domain.event.Event;

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
) {
    public static EventDto fromEvent(Event event) {
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getPreviewImageUrl(),
                event.getPartnersImageUrl(),
                event.getDescription(),
                event.getAmount(),
                event.getEventDate(),
                event.getLocation(),
                event.getUrlToPlatform()
        );
    }

    public static EventDto fromUpdateEventDto(UpdateEventDto updateEventDto) {
        return new EventDto(
                updateEventDto.id(),
                updateEventDto.title(),
                updateEventDto.previewImageUrl(),
                updateEventDto.partnersImageUrl(),
                updateEventDto.description(),
                updateEventDto.amount(),
                updateEventDto.eventDate(),
                updateEventDto.location(),
                updateEventDto.urlToPlatform()
        );
    }

    public Event toEvent() {
        return new Event(
                this.id,
                this.title,
                this.previewImageUrl,
                this.partnersImageUrl,
                this.description,
                this.amount,
                this.eventDate,
                this.location,
                this.urlToPlatform
        );
    }
}