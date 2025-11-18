package br.org.oficinadasmeninas.domain.donor.service;

import br.org.oficinadasmeninas.domain.donor.dto.DonorDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

/**
 * Serviço responsável pelo gerenciamento de doadores
 * <p>
 * Fornece uma operação para consulta de doadores,
 * </p>
 */
public interface IDonorService {

    /**
     * Retorna todos os doadores
     *
     * @return uma lista paginada com filtros de {@link DonorDto}
     */
    PageDTO<DonorDto> findAll(int page, int pageSize,
                              String donorBadge,
                              String sortField, String sortDirection,
                              String searchTerm);
}