package br.org.oficinadasmeninas.infra.event.service;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.domain.event.repository.IEventRepository;
import br.org.oficinadasmeninas.domain.event.service.IEventService;
import br.org.oficinadasmeninas.domain.objectstorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
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

    @Override
    public Event insert(CreateEventDto createEventDto) throws IOException {
        var previewFileName = uploadMultipartFile(createEventDto.previewImage());
        var partnersFileName = uploadMultipartFile(createEventDto.partnersImage());

        var createdEventId = eventRepository.insert(createEventDto, previewFileName, partnersFileName);

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

    @Override
    public Event update(UUID id, UpdateEventDto updateEventDto) throws Exception {
        eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.EVENT_NOT_FOUND + id));

        var previewFileName = uploadMultipartFile(updateEventDto.previewImage());
        var partnersFileName = uploadMultipartFile(updateEventDto.partnersImage());

        eventRepository.update(updateEventDto, previewFileName, partnersFileName);

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

    @Override
    public void deleteById(UUID id) {
        var event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.EVENT_NOT_FOUND + id));

        eventRepository.update(
                UpdateEventDto.forDeletion(event.getId()),
                event.getPreviewImageUrl(),
                event.getPartnersImageUrl()
        );
    }

    @Override
    public Event findById(UUID id) {

        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.EVENT_NOT_FOUND + id));
    }

    @Override
    public PageDTO<Event> getFilteredEvents(GetEventDto getEventDto){
        return eventRepository.findByFilter(getEventDto);
    }

    private String uploadMultipartFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty())
            return null;

        var fileName = storageService.sanitizeFileName(file.getOriginalFilename());

        storageService.upload(file, fileName, true);

        return fileName;
    }
}