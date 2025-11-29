package br.org.oficinadasmeninas.infra.event.service;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.domain.event.repository.IEventRepository;
import br.org.oficinadasmeninas.domain.event.service.IEventService;
import br.org.oficinadasmeninas.domain.objectstorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.infra.logging.Logging;
import br.org.oficinadasmeninas.infra.objectstorage.rollback.MinIoRollbackContext;
import br.org.oficinadasmeninas.infra.objectstorage.rollback.MinIoTransactional;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static br.org.oficinadasmeninas.domain.event.mapper.EventMapper.toEntity;

@Logging
@Service
public class EventService implements IEventService {

    private final IEventRepository eventRepository;
    private final IObjectStorage storageService;
    private final MinIoRollbackContext minIoRollbackContext;

    public EventService(IEventRepository eventRepository, IObjectStorage storageService, MinIoRollbackContext minIoRollbackContext) {
        this.eventRepository = eventRepository;
        this.storageService = storageService;
        this.minIoRollbackContext = minIoRollbackContext;
    }

    @Override
    @Transactional
    @MinIoTransactional
    public UUID insert(CreateEventDto request) {

        var previewImageUrl = storageService.uploadFile(request.previewImage(), true);
        var partnersImageUrl = storageService.uploadFile(request.partnersImage(), true);

        minIoRollbackContext.register(previewImageUrl, partnersImageUrl);

        var event = toEntity(request);
        event.setPreviewImageUrl(previewImageUrl);
        event.setPartnersImageUrl(partnersImageUrl);

        eventRepository.insert(event);
        return event.getId();
    }

    @Override
    @Transactional
    @MinIoTransactional
    public UUID update(UUID id, UpdateEventDto request) {

        var event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.EVENT_NOT_FOUND + id));

        var previewImageUrl = storageService.uploadFile(request.previewImage(), true);
        var partnersImageUrl = storageService.uploadFile(request.partnersImage(), true);

        minIoRollbackContext.register(previewImageUrl, partnersImageUrl);

        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setEventDate(request.eventDate());
        event.setLocation(request.location());
        event.setUrlToPlatform(request.urlToPlatform());
        event.setPreviewImageUrl(previewImageUrl);
        event.setPartnersImageUrl(partnersImageUrl);

        eventRepository.update(event);

        return event.getId();
    }

    @Override
    @Transactional
    public UUID deleteById(UUID id) {

        var event =  eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.EVENT_NOT_FOUND + id));

        eventRepository.deleteById(id);
        storageService.deleteFile(event.getPreviewImageUrl());
        storageService.deleteFile(event.getPartnersImageUrl());

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
}