package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.EventDto;
import br.org.oficinadasmeninas.domain.event.service.IEventService;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public PageDTO<Event> findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                  @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize) {
        return eventService.findAll(page, pageSize);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto addEvent(@ModelAttribute CreateEventDto createEventDto) {
        return eventService.createEvent(createEventDto);
    }
}