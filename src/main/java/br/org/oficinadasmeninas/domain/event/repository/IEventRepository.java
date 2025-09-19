package br.org.oficinadasmeninas.domain.event.repository;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.util.Optional;
import java.util.UUID;

public interface IEventRepository {
    Optional<Event> getEventById(UUID id);
    PageDTO<Event> findAll(int page, int pageSize);
    UUID createEvent(CreateEventDto createEventDto, String previewFileName, String partnerFileName);
    void updateEvent(UpdateEventDto eventDto, String previewFileName, String partnersFileName);
}