package br.org.oficinadasmeninas.domain.partner.service;

import br.org.oficinadasmeninas.domain.partner.dto.CreatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.dto.UpdatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.io.IOException;
import java.util.UUID;

public interface IPartnerService {
    PageDTO<Partner> findAll(int page, int pageSize);
    Partner findById(UUID id) throws Exception;
    Partner createPartner(CreatePartnerDto partnerDto) throws IOException;
    Partner updatePartner(UUID id, UpdatePartnerDto updatePartnerDto) throws Exception;
    void deletePartner(UUID id) throws Exception;
}