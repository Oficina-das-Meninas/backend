package br.org.oficinadasmeninas.infra.event.service;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.domain.event.repository.IEventRepository;
import br.org.oficinadasmeninas.domain.event.service.IEventService;
import br.org.oficinadasmeninas.domain.objectStorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.shared.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class EventService implements IEventService {
    private final IEventRepository eventRepository;
    private final IObjectStorage storageService;

    public EventService(IEventRepository eventRepository, IObjectStorage storageService) {
        this.eventRepository = eventRepository;
        this.storageService = storageService;
    }

    public PageDTO<Event> getFilteredEvents(GetEventDto getEventDto){
        return eventRepository.getFilteredEvents(getEventDto);
    }

    public Event findById(UUID id) {

        return eventRepository.getEventById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado: " + id));
    }

    public Event createEvent(CreateEventDto createEventDto) throws IOException {
        var previewFileName = uploadMultipartFile(createEventDto.previewImage());
        var partnersFileName = uploadMultipartFile(createEventDto.partnersImage());

        var createdEventId = eventRepository.createEvent(createEventDto, previewFileName, partnersFileName);

        return new Event(
                createdEventId,
                createEventDto.title(),
                previewFileName,
                partnersFileName,
                createEventDto.description(),
                createEventDto.eventDate(),
                createEventDto.location(),
                createEventDto.urlToPlatform()
        );
    }

    public Event updateEvent(UUID id, UpdateEventDto updateEventDto) throws Exception {
        eventRepository.getEventById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado: " + id));

        var previewFileName = uploadMultipartFile(updateEventDto.previewImage());
        var partnersFileName = uploadMultipartFile(updateEventDto.partnersImage());

        eventRepository.updateEvent(updateEventDto, previewFileName, partnersFileName);

        return new Event(
                id,
                updateEventDto.title(),
                previewFileName,
                partnersFileName,
                updateEventDto.description(),
                updateEventDto.eventDate(),
                updateEventDto.location(),
                updateEventDto.urlToPlatform()
        );
    }

    public void deleteEvent(UUID id) {
        var event = eventRepository.getEventById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado: " + id));

        event.setIsActive(false);

        eventRepository.updateEvent(UpdateEventDto.fromEvent(event), event.getPreviewImageUrl(), event.getPartnersImageUrl());
    }

    private String uploadMultipartFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty())
            return null;

        var fileName = storageService.sanitizeFileName(file.getOriginalFilename());

        storageService.upload(file, fileName, true);

        return fileName;
    }
}