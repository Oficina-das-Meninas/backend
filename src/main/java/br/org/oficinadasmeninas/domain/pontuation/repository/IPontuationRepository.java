package br.org.oficinadasmeninas.domain.pontuation.repository;

import br.org.oficinadasmeninas.domain.pontuation.Pontuation;
import br.org.oficinadasmeninas.domain.pontuation.dto.GetUserPontuationsDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.util.UUID;

public interface IPontuationRepository {
    PageDTO<Pontuation> findByIdAndFilters(UUID id, GetUserPontuationsDto getUserPontuationsDto);
}