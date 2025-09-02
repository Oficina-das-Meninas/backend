package br.org.oficinadasmeninas.domain.partner.service;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.shared.SearchDTO;
import org.springframework.data.domain.Page;

public interface IPartnerService {
    Page<Partner> findAll(SearchDTO partnerDTO);
}
