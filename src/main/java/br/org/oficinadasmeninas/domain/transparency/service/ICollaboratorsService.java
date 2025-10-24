package br.org.oficinadasmeninas.domain.transparency.service;

import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorRequestDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCollaboratorDto;

import java.util.UUID;

/**
 * Serviço responsável pelo gerenciamento de colaboradores no módulo de transparência.
 * <p>
 * Fornece operações para criação (upload) e exclusão de colaboradores,
 * geralmente associados a documentos ou informações públicas de transparência.
 * </p>
 */
public interface ICollaboratorsService {

    /**
     * Cria (realiza o upload de) um novo colaborador com base nos dados fornecidos.
     *
     * @param request objeto {@link CreateCollaboratorRequestDto} contendo as informações do colaborador; não deve ser {@code null}
     * @return o identificador único ({@link UUID}) do colaborador criado
     * @throws IllegalArgumentException se {@code request} for {@code null} ou contiver dados inválidos
     */
    UUID uploadCollaborator(CreateCollaboratorRequestDto request);

    /**
     * Remove um colaborador existente identificado pelo {@code id}.
     *
     * @param id identificador único ({@link UUID}) do colaborador a ser removido; não deve ser {@code null}
     * @return o identificador único ({@link UUID}) do colaborador removido
     * @throws IllegalArgumentException se {@code id} for {@code null}
     * @throws IllegalStateException se o colaborador não existir
     */
    UUID deleteCollaborator(UUID id);

    /**
     * Altera a prioridade de um colaborador existente identificado pelo {@code id}.
     *
     * @param id identificador único ({@link UUID}) do colaborador a ser alterado; não deve ser {@code null}
     * @return o identificador único ({@link UUID}) do colaborador alterado
     * @throws IllegalArgumentException se {@code id} for {@code null}
     * @throws IllegalStateException se o colaborador não existir
     */
    UUID updateCollaborator(UUID id, UpdateCollaboratorDto request);
}
