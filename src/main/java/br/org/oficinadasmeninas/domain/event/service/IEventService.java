package br.org.oficinadasmeninas.domain.event.service;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.io.IOException;
import java.util.UUID;

public interface IEventService {
    PageDTO<Event> findAll(int page, int pageSize);
    Event createEvent(CreateEventDto eventDto) throws IOException;
    Event updateEvent(UUID id, UpdateEventDto updateEventDto) throws Exception;
}