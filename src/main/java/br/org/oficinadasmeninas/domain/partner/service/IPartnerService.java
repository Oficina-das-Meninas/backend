package br.org.oficinadasmeninas.domain.partner.service;

import br.org.oficinadasmeninas.domain.partner.dto.CreatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.dto.UpdatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.io.IOException;
import java.util.UUID;

/**
 * Serviço responsável pelo gerenciamento de parceiros.
 * <p>
 * Define operações para listagem, consulta, criação, atualização e remoção
 * de registros de parceiros no sistema.
 */
public interface IPartnerService {

    /**
     * Recupera uma página de parceiros conforme os parâmetros de paginação informados.
     *
     * @param page índice da página a ser retornada (ex.: {@code 0} para a primeira página)
     * @param pageSize quantidade máxima de itens por página
     * @return {@link PageDTO} contendo os registros de {@link Partner} correspondentes à página solicitada;
     * pode estar vazio caso não existam parceiros na faixa especificada
     */
    PageDTO<Partner> findAll(String searchTerm, int page, int pageSize);

    /**
     * Recupera um parceiro específico a partir do seu identificador.
     *
     * @param id identificador único {@link UUID} do parceiro; não deve ser {@code null}
     * @return instância de {@link Partner} correspondente ao identificador informado
     * @throws Exception se o parceiro não for encontrado ou ocorrer falha durante a operação
     */
    Partner findById(UUID id) throws Exception;

    /**
     * Cria um novo parceiro com base nos dados fornecidos.
     *
     * @param partnerDto objeto {@link CreatePartnerDto} contendo as informações necessárias
     *                   para criação do parceiro; não deve ser {@code null}
     * @return instância de {@link Partner} criada e persistida
     * @throws IOException se ocorrer erro de entrada/saída durante o processo (por exemplo, upload de arquivos)
     */
    Partner createPartner(CreatePartnerDto partnerDto) throws IOException;

    /**
     * Atualiza as informações de um parceiro existente.
     *
     * @param id identificador único {@link UUID} do parceiro a ser atualizado; não deve ser {@code null}
     * @param updatePartnerDto objeto {@link UpdatePartnerDto} contendo os dados a serem atualizados;
     *                         não deve ser {@code null}
     * @return instância de {@link Partner} atualizada
     * @throws Exception se o parceiro não for encontrado ou ocorrer erro durante a atualização
     */
    Partner updatePartner(UUID id, UpdatePartnerDto updatePartnerDto) throws Exception;

    /**
     * Remove um parceiro identificado pelo seu identificador único.
     *
     * @param id identificador único {@link UUID} do parceiro a ser removido; não deve ser {@code null}
     * @throws Exception se o parceiro não for encontrado ou ocorrer erro durante a exclusão
     */
    void deletePartner(UUID id) throws Exception;
}
