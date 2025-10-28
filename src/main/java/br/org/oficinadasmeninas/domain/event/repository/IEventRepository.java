package br.org.oficinadasmeninas.domain.event.repository;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.util.Optional;
import java.util.UUID;

public interface IEventRepository {

    UUID insert(CreateEventDto createEventDto, String previewFileName, String partnerFileName);

    void update(UpdateEventDto eventDto, String previewFileName, String partnersFileName);

    PageDTO<Event> findByFilter(GetEventDto getEventDto);

    Optional<Event> findById(UUID id);
}