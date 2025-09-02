package br.org.oficinadasmeninas.domain.partner.repository;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.shared.SearchDTO;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface IPartnerRepository {
    Page<Partner> findAll(SearchDTO partnerDTO);
}
