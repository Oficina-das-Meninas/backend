package br.org.oficinadasmeninas.domain.transparency.service;

import br.org.oficinadasmeninas.domain.transparency.dto.CreateCategoryRequestDto;
import br.org.oficinadasmeninas.domain.transparency.dto.ResponseCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.GetCategoriesResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pelo gerenciamento de categorias do módulo de transparência.
 * <p>
 * Fornece operações para criação, atualização, exclusão e consulta de categorias,
 * incluindo consultas que retornam categorias juntamente com seus documentos associados.
 * </p>
 */
public interface ICategoriesService {

    /**
     * Cria uma nova categoria a partir dos dados fornecidos.
     *
     * @param request objeto {@link CreateCategoryRequestDto} contendo as informações da nova categoria; não deve ser {@code null}
     * @return o identificador único ({@link UUID}) da categoria criada
     * @throws IllegalArgumentException se o {@code request} for {@code null} ou contiver dados inválidos
     */
    UUID insert(CreateCategoryRequestDto request);

    /**
     * Atualiza uma categoria existente identificada pelo {@code id}.
     *
     * @param id identificador único ({@link UUID}) da categoria a ser atualizada; não deve ser {@code null}
     * @param request objeto {@link UpdateCategoryDto} contendo os campos a serem alterados; não deve ser {@code null}
     * @return o identificador único ({@link UUID}) da categoria atualizada
     * @throws IllegalArgumentException se {@code id} ou {@code request} forem {@code null}
     * @throws IllegalStateException se a categoria não existir
     */
    UUID update(UUID id, UpdateCategoryDto request);

    /**
     * Exclui uma categoria com base no seu identificador.
     *
     * @param id identificador único ({@link UUID}) da categoria a ser removida; não deve ser {@code null}
     * @return o identificador único ({@link UUID}) da categoria excluída
     * @throws IllegalArgumentException se {@code id} for {@code null}
     * @throws IllegalStateException se a categoria não existir
     */
    UUID deleteById(UUID id);

    /**
     * Retorna os detalhes de uma categoria específica.
     *
     * @param id identificador único ({@link UUID}) da categoria; não deve ser {@code null}
     * @return um {@link ResponseCategoryDto} contendo as informações da categoria, ou {@code null} se não encontrada
     * @throws IllegalArgumentException se {@code id} for {@code null}
     */
    ResponseCategoryDto findById(UUID id);

    /**
     * Retorna todas as categorias existentes.
     *
     * @return uma lista de {@link ResponseCategoryDto}; pode ser vazia se não houver categorias cadastradas
     */
    List<ResponseCategoryDto> findAll();

    /**
     * Retorna todas as categorias juntamente com seus documentos associados.
     * <p>
     * Essa operação é útil para exibição de dados estruturados em interfaces de transparência,
     * como portais públicos ou dashboards administrativos.
     * </p>
     *
     * @return um {@link GetCategoriesResponseDto} contendo a lista de categorias e seus respectivos documentos / colaboradores
     */
    GetCategoriesResponseDto findAllWithDocumentsAndCollaborators();
}
