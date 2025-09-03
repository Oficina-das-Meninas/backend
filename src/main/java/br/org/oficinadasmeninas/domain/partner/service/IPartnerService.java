package br.org.oficinadasmeninas.domain.partner.service;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

public interface IPartnerService {
    PageDTO<Partner> findAll(int page, int pageSize);
}
