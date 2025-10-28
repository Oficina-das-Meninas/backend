package br.org.oficinadasmeninas.presentation.controller;

import java.util.Date;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCategoryRequestDto;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorRequestDto;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentRequestDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCollaboratorDto;
import br.org.oficinadasmeninas.domain.transparency.service.ICategoriesService;
import br.org.oficinadasmeninas.domain.transparency.service.ICollaboratorsService;
import br.org.oficinadasmeninas.domain.transparency.service.IDocumentsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

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
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") @NotBlank String title,
            @RequestParam("effectiveDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date effectiveDate,
            @RequestParam("categoryId") @NotBlank String categoryId
    ) {
        var request = new CreateDocumentRequestDto(file, title, effectiveDate, categoryId);

        return handle(
            () -> documentsService.create(request),
            Messages.DOCUMENT_CREATED_SUCCESSFULLY,
            HttpStatus.CREATED
        );
    }

    @PostMapping("/collaborators")
    public ResponseEntity<?> uploadCollaborator(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") @NotBlank String name,
            @RequestParam("role") String role,
            @RequestParam("description") String description,
            @RequestParam("priority") @NotBlank String priority,
            @RequestParam("categoryId") @NotBlank String categoryId
    ) {
        var request = new CreateCollaboratorRequestDto(image, name, role, description, categoryId);

        return handle(
            () -> collaboratorsService.uploadCollaborator(request),
            Messages.COLLABORATOR_CREATED_SUCCESSFULLY,
            HttpStatus.CREATED
        );
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<?> deleteDocument(
            @PathVariable("documentId") @NotBlank String documentId
    ) {
        return handle(
            () -> documentsService.deleteById(UUID.fromString(documentId)),
            Messages.DOCUMENT_DELETED_SUCCESSFULLY
        );
    }

    @PatchMapping("/collaborators/{collaboratorId}")
    public ResponseEntity<?> updateCollaborator(
            @PathVariable("collaboratorId") @NotBlank String collaboratorId,
            @Valid @RequestBody UpdateCollaboratorDto request
    ) {
        var id = UUID.fromString(collaboratorId);

        return handle(
                () -> collaboratorsService.updateCollaborator(id, request),
                Messages.COLLABORATOR_UPDATED_SUCCESSFULLY
        );
    }

    @DeleteMapping("/collaborators/{collaboratorId}")
    public ResponseEntity<?> deleteCollaborator(
            @PathVariable("collaboratorId") @NotBlank String collaboratorId
    ) {
        var id = UUID.fromString(collaboratorId);

        return handle(
            () -> collaboratorsService.deleteCollaborator(id),
            Messages.COLLABORATOR_DELETED_SUCCESSFULLY
        );
    }

    @PostMapping("/categories")
    public ResponseEntity<?> insertCategory(
            @Valid @RequestBody CreateCategoryRequestDto request
    ) {
        return handle(
            () -> categoriesService.insertCategory(request),
            Messages.CATEGORY_CREATED_SUCCESSFULLY,
            HttpStatus.CREATED
        );
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategoryById(
            @PathVariable UUID id
    ) {
        return handle(() -> categoriesService.getCategoryById(id));
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        return handle(categoriesService::getAllCategories);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {

        return handle(categoriesService::getAllCategoriesWithDocuments);
    }

    @PatchMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCategoryDto request
    ) {
        return handle(
            () -> categoriesService.updateCategory(id, request),
            Messages.CATEGORY_UPDATED_SUCCESSFULLY
        );
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable UUID id
    ) {
        return handle(
            () -> categoriesService.deleteCategory(id),
            Messages.CATEGORY_DELETED_SUCCESSFULLY
        );
    }
}