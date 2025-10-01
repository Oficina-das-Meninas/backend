package br.org.oficinadasmeninas.domain.transparency.service;

import br.org.oficinadasmeninas.domain.transparency.dto.CreateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.ResponseCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.GetCategoriesResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Interface para operações relacionadas à transparência institucional.
 *
 * Define contratos para manipulação de documentos, colaboradores e categorias,
 * incluindo operações de upload, consulta, atualização e exclusão.
 */
public interface ITransparencyService {

    /**
     * Faz upload de um documento vinculado a uma categoria.
     *
     * @param file          arquivo do documento (formatos aceitos: PDF, DOCX, etc.)
     * @param title         título do documento
     * @param effectiveDate data de vigência do documento
     * @param categoryId    identificador da categoria à qual o documento será associado
     * @throws IOException em caso de erro de leitura/gravação do arquivo
     */
    void uploadDocument(MultipartFile file, String title, Date effectiveDate, String categoryId) throws IOException;

    /**
     * Faz upload de um colaborador, incluindo foto e informações relacionadas.
     *
     * @param file        foto do colaborador (formatos aceitos: JPG, PNG; tamanho máximo recomendado: 5 MB)
     * @param name        nome completo do colaborador
     * @param role        função ou cargo ocupado
     * @param description breve descrição ou biografia do colaborador
     * @param priority    prioridade de exibição (ex: "ALTA", "MEDIA", "BAIXA")
     * @param categoryId  identificador da categoria à qual o colaborador será associado
     * @throws IOException em caso de erro de leitura/gravação do arquivo
     */
    void uploadCollaborator(MultipartFile file, String name, String role, String description, String priority, String categoryId) throws IOException;

    /**
     * Exclui um colaborador pelo seu identificador único.
     *
     * @param id identificador único do colaborador
     * @throws IOException em caso de erro durante a exclusão
     */
    void deleteCollaborator(UUID id) throws IOException;

    /**
     * Exclui um documento pelo seu identificador único.
     *
     * @param id identificador único do documento
     * @throws IOException em caso de erro durante a exclusão
     */
    void deleteDocument(UUID id) throws IOException;

    /**
     * Cria e insere uma nova categoria de documentos ou colaboradores.
     *
     * @param request dados necessários para criação da categoria
     * @return objeto de resposta contendo informações da categoria criada
     */
    ResponseCategoryDto insertCategory(CreateCategoryDto request);

    /**
     * Busca uma categoria pelo seu identificador.
     *
     * @param id identificador único da categoria
     * @return objeto de resposta contendo informações da categoria encontrada
     */
    ResponseCategoryDto getCategoryById(UUID id);

    /**
     * Retorna todas as categorias existentes.
     *
     * @return lista contendo todas as categorias cadastradas
     */
    List<ResponseCategoryDto> getAllCategories();

    /**
     * Retorna todas as categorias com seus documentos associados.
     *
     * @return objeto de resposta contendo categorias e suas respectivas listas de documentos
     */
    GetCategoriesResponseDto getAllCategoriesWithDocuments();

    /**
     * Atualiza uma categoria existente com novos dados.
     *
     * @param id      identificador único da categoria a ser atualizada
     * @param request dados de atualização da categoria
     * @return objeto de resposta contendo informações da categoria atualizada
     */
    ResponseCategoryDto updateCategory(UUID id, UpdateCategoryDto request);

    /**
     * Exclui uma categoria pelo seu identificador.
     * <p>
     * Observação: dependendo da regra de negócio, a exclusão pode impactar documentos
     * e colaboradores vinculados. Recomenda-se verificar dependências antes da operação.
     * </p>
     *
     * @param id identificador único da categoria
     */
    void deleteCategory(UUID id);

}
