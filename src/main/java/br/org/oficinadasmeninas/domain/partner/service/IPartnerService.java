package br.org.oficinadasmeninas.domain.partner.service;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

/**
 * Interface para operações relacionadas à gestão de parceiros.
 *
 * Define contratos para consulta e paginação de parceiros cadastrados.
 */
public interface IPartnerService {

    /**
     * Retorna uma página contendo parceiros cadastrados no sistema.
     *
     * @param page     número da página a ser retornada (baseado em índice, iniciando em 0 ou 1 conforme convenção adotada)
     * @param pageSize quantidade de itens por página
     * @return objeto de paginação contendo a lista de parceiros e informações adicionais de paginação
     */
    PageDTO<Partner> findAll(int page, int pageSize);

}
