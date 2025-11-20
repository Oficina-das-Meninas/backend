package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.domain.event.service.IEventService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class EventController extends BaseController {
    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Cria um novo evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do Evento"),
            @ApiResponse(responseCode = "503", description = "Erro ao executar o Bucket")
    })
    @PostMapping
    public ResponseEntity<?> createEvent(
            @ModelAttribute @Valid CreateEventDto createEventDto
    ) {
        return handle(
                () -> eventService.insert(createEventDto),
                Messages.EVENT_CREATED_SUCCESSFULLY,
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Atualiza um Evento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização do Evento"),
            @ApiResponse(responseCode = "503", description = "Erro ao executar o Bucket")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(
            @PathVariable UUID id,
            @ModelAttribute @Valid UpdateEventDto updateEventDto
    ) {
        return handle(
                () -> eventService.update(id, updateEventDto),
                Messages.EVENT_UPDATED_SUCCESSFULLY
        );
    }

    @Operation(summary = "Exclui um Evento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento excluido com sucesso"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(
            @PathVariable UUID id
    ) {
        return handle(
                () -> eventService.deleteById(id),
                Messages.EVENT_DELETED_SUCCESSFULLY
        );
    }

    @Operation(summary = "Lista eventos filtrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventos encontrados com sucesso")
    })
    @GetMapping
    @PreAuthorize("isAnonymous() or hasAnyRole('ADMIN', 'USER')")
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

    @Operation(summary = "Busca um evento pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAnonymous() or hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> findEventById(
            @PathVariable UUID id
    ) {
        return handle(() -> eventService.findById(id));
    }
}