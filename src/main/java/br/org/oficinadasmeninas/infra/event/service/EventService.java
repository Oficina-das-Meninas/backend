package br.org.oficinadasmeninas.infra.event.service;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.domain.event.repository.IEventRepository;
import br.org.oficinadasmeninas.domain.event.service.IEventService;
import br.org.oficinadasmeninas.domain.objectStorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.shared.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
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

    public PageDTO<Event> findAll(@RequestParam @PositiveOrZero int page,
                                  @RequestParam @Positive @Max(100) int pageSize){
        return eventRepository.findAll(page, pageSize);
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
                createEventDto.amount(),
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
                updateEventDto.amount(),
                updateEventDto.eventDate(),
                updateEventDto.location(),
                updateEventDto.urlToPlatform()
        );
    }

    private String uploadMultipartFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty())
            return null;

        var fileName = storageService.sanitizeFileName(file.getOriginalFilename());

        storageService.upload(file, fileName, true);

        return fileName;
    }
}