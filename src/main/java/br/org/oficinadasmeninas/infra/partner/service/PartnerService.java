package br.org.oficinadasmeninas.infra.partner.service;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.partner.repository.IPartnerRepository;
import br.org.oficinadasmeninas.domain.partner.service.IPartnerService;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class PartnerService implements IPartnerService{
    private final IPartnerRepository partnerRepository;

    public PartnerService(IPartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    public PageDTO<Partner> findAll(@RequestParam @PositiveOrZero int page,
                                    @RequestParam @Positive @Max(100) int pageSize){
        return partnerRepository.findAll(page, pageSize);
    }
}