package br.org.oficinadasmeninas.infra.event.service;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.domain.event.repository.IEventRepository;
import br.org.oficinadasmeninas.domain.event.service.IEventService;
import br.org.oficinadasmeninas.domain.objectstorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.infra.exceptions.ObjectStorageException;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static br.org.oficinadasmeninas.domain.event.mapper.EventMapper.toEntity;

@Service
public class EventService implements IEventService {
    private final IEventRepository eventRepository;
    private final IObjectStorage storageService;

    public EventService(IEventRepository eventRepository, IObjectStorage storageService) {
        this.eventRepository = eventRepository;
        this.storageService = storageService;
    }

    @Override
    public UUID insert(CreateEventDto request) {

        var previewFileName = uploadMultipartFile(request.previewImage());
        var partnersFileName = uploadMultipartFile(request.partnersImage());

        var event = toEntity(request);
        event.setPreviewImageUrl(previewFileName);
        event.setPartnersImageUrl(partnersFileName);

        eventRepository.insert(event);
        return event.getId();
    }

    @Override
    public UUID update(UUID id, UpdateEventDto request) {

        var event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.EVENT_NOT_FOUND + id));

        var previewFileName = uploadMultipartFile(request.previewImage());
        var partnersFileName = uploadMultipartFile(request.partnersImage());

        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setEventDate(request.eventDate());
        event.setLocation(request.location());
        event.setUrlToPlatform(request.urlToPlatform());
        event.setPreviewImageUrl(previewFileName);
        event.setPartnersImageUrl(partnersFileName);

        eventRepository.update(event);

        return event.getId();
    }

    @Override
    public UUID deleteById(UUID id) {
    	eventRepository.deleteById(id);
        return id;
    }

    @Override
    public Event findById(UUID id) {

        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.EVENT_NOT_FOUND + id));
    }

    @Override
    public PageDTO<Event> findByFilter(GetEventDto getEventDto) {
        return eventRepository.findByFilter(getEventDto);
    }

    private String uploadMultipartFile(MultipartFile file) {
        if (file == null || file.isEmpty())
            return null;

        var fileName = storageService.sanitizeFileName(file.getOriginalFilename());

        storageService.uploadWithName(file, fileName, true);

        return fileName;
    }
}