package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.partner.dto.CreatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.dto.UpdatePartnerDto;
import br.org.oficinadasmeninas.infra.partner.service.PartnerService;
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
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/partners")
public class PartnerController {
    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping
    public PageDTO<Partner> findAll(@RequestParam @Nullable String searchTerm,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                    @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize){
        return partnerService.findAll(searchTerm, page, pageSize);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Partner findPartnerById(@PathVariable UUID id) {
        return partnerService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Partner createPartner(@ModelAttribute @Valid CreatePartnerDto createPartnerDto) throws IOException {
        return partnerService.createPartner(createPartnerDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Partner updatePartner(
            @PathVariable UUID id,
            @ModelAttribute @Valid UpdatePartnerDto updatePartnerDto) throws Exception {

        return partnerService.updatePartner(id, updatePartnerDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePartner(@PathVariable UUID id) {
        partnerService.deletePartner(id);
    }
}