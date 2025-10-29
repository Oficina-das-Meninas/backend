package br.org.oficinadasmeninas.domain.event.mapper;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;

public final class EventMapper {

    public static Event toEntity(CreateEventDto request) {
        var event = new Event();
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setEventDate(request.eventDate());
        event.setLocation(request.location());
        event.setUrlToPlatform(request.urlToPlatform());

        return event;
    }
}
