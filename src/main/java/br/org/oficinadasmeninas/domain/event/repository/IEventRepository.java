package br.org.oficinadasmeninas.domain.event.repository;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.util.UUID;

public interface IEventRepository {
    PageDTO<Event> findAll(int page, int pageSize);
    UUID createEvent(CreateEventDto eventDto);
    void updateEvent(UpdateEventDto eventDto);
}