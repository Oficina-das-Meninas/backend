package br.org.oficinadasmeninas.domain.donor.repository;

import br.org.oficinadasmeninas.domain.donor.dto.DonorDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

public interface IDonorRepository {

    PageDTO<DonorDto> findAll(int page, int pageSize,
                              String donorBadge,
                              String sortField, String sortDirection,
                              String searchTerm);
}