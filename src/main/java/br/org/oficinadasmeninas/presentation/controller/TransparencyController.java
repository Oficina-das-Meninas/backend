package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.transparency.dto.*;
import br.org.oficinadasmeninas.domain.transparency.service.ICategoriesService;
import br.org.oficinadasmeninas.domain.transparency.service.ICollaboratorsService;
import br.org.oficinadasmeninas.domain.transparency.service.IDocumentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/transparencies")
public class TransparencyController extends BaseController {

    private final IDocumentsService documentsService;
    private final ICollaboratorsService collaboratorsService;
    private final ICategoriesService categoriesService;

    public TransparencyController(IDocumentsService documentsService, ICollaboratorsService collaboratorsService, ICategoriesService categoriesService) {
        this.documentsService = documentsService;
        this.collaboratorsService = collaboratorsService;
        this.categoriesService = categoriesService;
    }

    @Operation(summary = "Faz upload de um documento de transparência")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Documento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados"),
            @ApiResponse(responseCode = "404", description = "Caso não encontre a Categoria"),
            @ApiResponse(responseCode = "503", description = "Erro ao executar o Bucket")
    })
    @PostMapping("/documents")
    public ResponseEntity<?> uploadDocument(
            @Valid @ModelAttribute CreateDocumentRequestDto request
    ) {
        return handle(
                () -> documentsService.create(request),
                Messages.DOCUMENT_CREATED_SUCCESSFULLY,
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Faz upload de um colaborador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Colaborador criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados"),
            @ApiResponse(responseCode = "404", description = "Caso não encontre a Categoria"),
            @ApiResponse(responseCode = "503", description = "Erro ao executar o Bucket")
    })
    @PostMapping("/collaborators")
    public ResponseEntity<?> uploadCollaborator(
            @Valid @ModelAttribute CreateCollaboratorRequestDto request
    ) {
        return handle(
                () -> collaboratorsService.insert(request),
                Messages.COLLABORATOR_CREATED_SUCCESSFULLY,
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Remove um documento pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Documento não encontrado"),
            @ApiResponse(responseCode = "503", description = "Erro ao executar o Bucket")
    })
    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<?> deleteDocument(
            @PathVariable("documentId") UUID documentId
    ) {
        return handle(
                () -> documentsService.deleteById(documentId),
                Messages.DOCUMENT_DELETED_SUCCESSFULLY
        );
    }

    @Operation(summary = "Atualiza um colaborador existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Colaborador atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Colaborador não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados")
    })
    @PatchMapping("/collaborators/{collaboratorId}")
    public ResponseEntity<?> updateCollaborator(
            @PathVariable("collaboratorId") UUID collaboratorId,
            @Valid @RequestBody UpdateCollaboratorDto request
    ) {
        return handle(
                () -> collaboratorsService.update(collaboratorId, request),
                Messages.COLLABORATOR_UPDATED_SUCCESSFULLY
        );
    }

    @Operation(summary = "Remove um colaborador pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Colaborador removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Colaborador não encontrado"),
            @ApiResponse(responseCode = "503", description = "Erro ao executar o Bucket")
    })
    @DeleteMapping("/collaborators/{collaboratorId}")
    public ResponseEntity<?> deleteCollaborator(
            @PathVariable("collaboratorId") UUID collaboratorId
    ) {
        return handle(
                () -> collaboratorsService.deleteById(collaboratorId),
                Messages.COLLABORATOR_DELETED_SUCCESSFULLY
        );
    }

    @Operation(summary = "Cria uma nova categoria de transparência")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados")
    })
    @PostMapping("/categories")
    public ResponseEntity<?> insertCategory(
            @Valid @RequestBody CreateCategoryRequestDto request
    ) {
        return handle(
                () -> categoriesService.insert(request),
                Messages.CATEGORY_CREATED_SUCCESSFULLY,
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Busca uma categoria pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategoryById(
            @PathVariable UUID id
    ) {
        return handle(() -> categoriesService.findById(id));
    }

    @Operation(summary = "Lista todas as categorias de transparência")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorias listadas com sucesso")
    })
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        return handle(categoriesService::findAll);
    }

    @Operation(summary = "Retorna toda a árvore de transparência (categorias, documentos e colaboradores)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados recuperados com sucesso")
    })
    @GetMapping
    public ResponseEntity<?> getAll() {

        return handle(categoriesService::findAllWithDocumentsAndCollaborators);
    }

    @Operation(summary = "Atualiza uma categoria de transparência")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @PatchMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCategoryDto request
    ) {
        return handle(
                () -> categoriesService.update(id, request),
                Messages.CATEGORY_UPDATED_SUCCESSFULLY
        );
    }

    @Operation(summary = "Remove uma categoria pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "400", description = "Existem documentos e/ou colaboradores atrelados")
    })
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable UUID id
    ) {
        return handle(
                () -> categoriesService.deleteById(id),
                Messages.CATEGORY_DELETED_SUCCESSFULLY
        );
    }
}