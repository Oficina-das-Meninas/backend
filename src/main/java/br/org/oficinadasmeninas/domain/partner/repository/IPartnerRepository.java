package br.org.oficinadasmeninas.domain.partner.repository;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

public interface IPartnerRepository {
    PageDTO<Partner> findAll(int page, int pageSize);
}
