package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.transparency.dto.*;
import br.org.oficinadasmeninas.domain.transparency.service.ICategoriesService;
import br.org.oficinadasmeninas.domain.transparency.service.ICollaboratorsService;
import br.org.oficinadasmeninas.domain.transparency.service.IDocumentsService;
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

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<?> deleteDocument(
            @PathVariable("documentId") @NotBlank UUID documentId
    ) {
        return handle(
                () -> documentsService.deleteById(documentId),
                Messages.DOCUMENT_DELETED_SUCCESSFULLY
        );
    }

    @PatchMapping("/collaborators/{collaboratorId}")
    public ResponseEntity<?> updateCollaborator(
            @PathVariable("collaboratorId") @NotBlank UUID collaboratorId,
            @Valid @RequestBody UpdateCollaboratorDto request
    ) {
        return handle(
                () -> collaboratorsService.update(collaboratorId, request),
                Messages.COLLABORATOR_UPDATED_SUCCESSFULLY
        );
    }

    @DeleteMapping("/collaborators/{collaboratorId}")
    public ResponseEntity<?> deleteCollaborator(
            @PathVariable("collaboratorId") @NotBlank UUID collaboratorId
    ) {
        return handle(
                () -> collaboratorsService.deleteById(collaboratorId),
                Messages.COLLABORATOR_DELETED_SUCCESSFULLY
        );
    }

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

    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategoryById(
            @PathVariable UUID id
    ) {
        return handle(() -> categoriesService.findById(id));
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        return handle(categoriesService::findAll);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {

        return handle(categoriesService::findAllWithDocumentsAndCollaborators);
    }

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