package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.domain.event.service.IEventService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@Validated
public class EventController extends BaseController {
    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createEvent(
            @ModelAttribute @Valid CreateEventDto createEventDto
    ) {
        return handle(
                () -> eventService.insert(createEventDto),
                Messages.EVENT_CREATED_SUCCESSFULLY,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateEvent(
            @PathVariable UUID id,
            @ModelAttribute @Valid UpdateEventDto updateEventDto
    ) {
        return handle(
                () -> eventService.update(id, updateEventDto),
                Messages.EVENT_UPDATED_SUCCESSFULLY
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(
            @PathVariable UUID id
    ) {
        return handle(
                () -> eventService.deleteById(id),
                Messages.EVENT_DELETED_SUCCESSFULLY
        );
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getFilteredEvents(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize,
            @RequestParam @Nullable String searchTerm,
            @RequestParam @Nullable String description,
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalDate endDate
    ) {

        var request = GetEventDto.FromRequestParams(page, pageSize, searchTerm, description, startDate, endDate);

        return handle(() -> eventService.findByFilter(request));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> findEventById(
            @PathVariable UUID id
    ) {
        return handle(() -> eventService.findById(id));
    }
}