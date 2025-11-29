package br.org.oficinadasmeninas.infra.donor.service;

import br.org.oficinadasmeninas.domain.donor.dto.DonorDto;
import br.org.oficinadasmeninas.domain.donor.repository.IDonorRepository;
import br.org.oficinadasmeninas.domain.donor.service.IDonorService;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.stereotype.Service;

@Service
public class DonorService implements IDonorService {

    private final IDonorRepository donorRepository;

    public DonorService(IDonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    @Override
    public PageDTO<DonorDto> findAll(int page, int pageSize,
                                     String donorBadge,
                                     String sortField, String sortDirection,
                                     String searchTerm) {

        return donorRepository.findAll(page, pageSize, donorBadge, sortField, sortDirection, searchTerm);
    }
}