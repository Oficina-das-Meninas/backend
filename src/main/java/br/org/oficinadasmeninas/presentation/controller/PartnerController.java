package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.partner.dto.CreatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.dto.UpdatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.service.IPartnerService;
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

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/partners")
public class PartnerController extends BaseController {

    private final IPartnerService partnerService;

    public PartnerController(IPartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPartner(
            @ModelAttribute @Valid CreatePartnerDto createPartnerDto
    ) {
        return handle(
                () -> partnerService.insert(createPartnerDto),
                Messages.PARTNER_CREATED_SUCCESSFULLY,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updatePartner(
            @PathVariable UUID id,
            @ModelAttribute @Valid UpdatePartnerDto updatePartnerDto
    ) {
        return handle(
                () -> partnerService.update(id, updatePartnerDto),
                Messages.PARTNER_UPDATED_SUCCESSFULLY
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deletePartner(@PathVariable UUID id) {

        return handle(
                () -> partnerService.deleteById(id),
                Messages.PARTNER_DELETED_SUCCESSFULLY
        );
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam @Nullable String searchTerm,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize
    ) {
        return handle(() -> partnerService.findAll(searchTerm, page, pageSize));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> findPartnerById(@PathVariable UUID id) {
        return handle(() -> partnerService.findById(id));
    }
}