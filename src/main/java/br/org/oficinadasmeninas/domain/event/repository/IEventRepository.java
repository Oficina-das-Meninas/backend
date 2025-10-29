package br.org.oficinadasmeninas.domain.event.repository;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.util.Optional;
import java.util.UUID;

public interface IEventRepository {

    Event insert(Event event);

    Event update(Event event, boolean isActive);

    PageDTO<Event> findByFilter(GetEventDto getEventDto);

    Optional<Event> findById(UUID id);
}