package br.org.oficinadasmeninas.domain.pontuation.service;

import br.org.oficinadasmeninas.domain.pontuation.dto.GetUserPontuationsDto;
import br.org.oficinadasmeninas.domain.pontuation.dto.PontuationDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.util.UUID;

/**
 * Define as operações relacionadas ao gerenciamento de pontuações de usuários no sistema.
 * <p>
 * Essa interface é responsável por recuperar informações sobre os pontos ganhos
 * e valores doados pelos usuários, com suporte a filtros e paginação.
 */
public interface IPontuationService {

    /**
     * Retorna as pontuações de um usuário com base nos parâmetros fornecidos.
     * <p>
     * Os resultados podem ser filtrados por período, método de doação e descrição.
     * A resposta é paginada conforme os parâmetros de página e tamanho da página.
     *
     * @param id identificador único do usuário
     * @param getUserPontuationsDTO objeto contendo os critérios de busca e paginação
     * @return página de {@link PontuationDto} filtrados conforme os critérios informados e representando as pontuações do usuário
     */
    PageDTO<PontuationDto> getUserPontuations(UUID id, GetUserPontuationsDto getUserPontuationsDTO);
}