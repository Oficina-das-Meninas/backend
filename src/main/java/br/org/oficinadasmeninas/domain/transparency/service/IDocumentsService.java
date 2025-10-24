package br.org.oficinadasmeninas.domain.transparency.service;

import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentRequestDto;

import java.util.UUID;

/**
 * Serviço responsável pelo gerenciamento de documentos no módulo de transparência.
 * <p>
 * Fornece operações para criação (upload) e exclusão de documentos,
 * geralmente associados a categorias ou colaboradores dentro do sistema de transparência.
 * </p>
 */
public interface IDocumentsService {

    /**
     * Cria (realiza o upload de) um novo documento com base nos dados fornecidos.
     *
     * @param request objeto {@link CreateDocumentRequestDto} contendo as informações do documento; não deve ser {@code null}
     * @return o identificador único ({@link UUID}) do documento criado
     * @throws IllegalArgumentException se {@code request} for {@code null} ou contiver dados inválidos
     */
    UUID uploadDocument(CreateDocumentRequestDto request);

    /**
     * Remove um documento existente identificado pelo {@code id}.
     *
     * @param id identificador único ({@link UUID}) do documento a ser removido; não deve ser {@code null}
     * @return o identificador único ({@link UUID}) do documento removido
     * @throws IllegalArgumentException se {@code id} for {@code null}
     * @throws IllegalStateException se o documento não existir
     */
    UUID deleteDocument(UUID id);
}
