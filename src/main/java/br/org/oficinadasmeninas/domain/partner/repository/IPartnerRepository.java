package br.org.oficinadasmeninas.domain.partner.repository;

import br.org.oficinadasmeninas.domain.partner.dto.CreatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.dto.UpdatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.util.Optional;
import java.util.UUID;

public interface IPartnerRepository {
    PageDTO<Partner> findAll(int page, int pageSize);
    Optional<Partner> getPartnerById(UUID id);
    UUID createPartner(CreatePartnerDto createPartnerDto, String previewFileName);
    void updatePartner(UpdatePartnerDto partnerDto, String previewFileName);
}
