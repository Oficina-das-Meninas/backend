package br.org.oficinadasmeninas.domain.partner.repository;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.util.Optional;
import java.util.UUID;

public interface IPartnerRepository {

    Partner insert(Partner partner);

    Partner update(Partner partner);

    PageDTO<Partner> findByFilter(String searchTerm, int page, int pageSize);

    Optional<Partner> findById(UUID id);
    
    void deleteById(UUID id);
}
