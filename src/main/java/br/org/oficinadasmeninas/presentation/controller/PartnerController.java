package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.shared.SearchDTO;
import br.org.oficinadasmeninas.infra.partner.service.PartnerService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {
    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Partner> findAll(SearchDTO partnerDTO){
        return partnerService.findAll(partnerDTO);
    }
}
