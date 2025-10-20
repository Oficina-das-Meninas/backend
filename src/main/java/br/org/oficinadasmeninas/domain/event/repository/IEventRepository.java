package br.org.oficinadasmeninas.domain.event.repository;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.util.Optional;
import java.util.UUID;

public interface IEventRepository {
    PageDTO<Event> getFiltered(GetEventDto getEventDto);
    Optional<Event> getById(UUID id);
    UUID create(CreateEventDto createEventDto, String previewFileName, String partnerFileName);
    void update(UpdateEventDto eventDto, String previewFileName, String partnersFileName);
}