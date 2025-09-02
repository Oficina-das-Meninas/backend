package br.org.oficinadasmeninas.infra.partner.service;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.partner.repository.IPartnerRepository;
import br.org.oficinadasmeninas.domain.partner.service.IPartnerService;
import br.org.oficinadasmeninas.domain.shared.SearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PartnerService implements IPartnerService{
    private final IPartnerRepository partnerRepository;

    public PartnerService(IPartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    public Page<Partner> findAll(SearchDTO partnerDTO){
        return partnerRepository.findAll(partnerDTO);
    }
}