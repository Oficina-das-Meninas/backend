package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.infra.partner.service.PartnerService;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/partners")
public class PartnerController {
    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping
    public PageDTO<Partner> findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                    @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize ){
        return partnerService.findAll(page, pageSize);
    }
}
