package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.domain.event.service.IEventService;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@Validated
public class EventController {
    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public PageDTO<Event> getFilteredEvents(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                            @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize,
                                            @RequestParam @Nullable String searchTerm,
                                            @RequestParam @Nullable String description,
                                            @RequestParam @Nullable LocalDate startDate,
                                            @RequestParam @Nullable LocalDate endDate
    ) {
        return eventService.getFilteredEvents(
                GetEventDto.FromRequestParams(page, pageSize, searchTerm, description, startDate, endDate)
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Event findEventById(@PathVariable UUID id) throws Exception {
        return eventService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@ModelAttribute @Valid CreateEventDto createEventDto) throws IOException {
        return eventService.createEvent(createEventDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Event updateEvent(
            @PathVariable UUID id,
            @ModelAttribute @Valid UpdateEventDto updateEventDto) throws Exception {

        return eventService.updateEvent(id, updateEventDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable UUID id) throws Exception {
        eventService.deleteEvent(id);
    }
}