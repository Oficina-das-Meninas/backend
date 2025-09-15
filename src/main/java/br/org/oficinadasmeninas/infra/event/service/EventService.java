package br.org.oficinadasmeninas.infra.event.service;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.EventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.domain.event.repository.IEventRepository;
import br.org.oficinadasmeninas.domain.event.service.IEventService;
import br.org.oficinadasmeninas.domain.objectStorage.IObjectStorage;
import br.org.oficinadasmeninas.infra.ObjectStorage.MinIoImplementation;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    public EventDto createEvent(CreateEventDto createEventDto) throws IOException {
        var createdEventId = eventRepository.createEvent(createEventDto);
        var fileName = saveOrUploadMultipartFile(createEventDto.getEventImage());

        createEventDto.setId(createdEventId);
        createEventDto.setPreviewImageUrl(fileName);

        return EventDto.fromCreateEventDto(createEventDto);
    }

    public EventDto updateEvent(UpdateEventDto updateEventDto) throws IOException {
        eventRepository.updateEvent(updateEventDto);

        var fileName = saveOrUploadMultipartFile(updateEventDto.getEventImage());

        updateEventDto.setPreviewImageUrl(fileName);

        return EventDto.fromUpdateEventDto(updateEventDto);
    }

    private String saveOrUploadMultipartFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty())
            return null;

        String fileName = file.getOriginalFilename();
        String newFileName = MinIoImplementation.generateTitle(fileName);
        storageService.upload(newFileName, file.getInputStream(), file.getContentType());

        return newFileName;
    }
}