package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.transparency.dto.*;
import br.org.oficinadasmeninas.infra.transparency.exception.CollaboratorNotFoundException;
import br.org.oficinadasmeninas.infra.transparency.exception.DocumentNotFoundException;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.GetCategoriesResponseDto;
import br.org.oficinadasmeninas.domain.transparency.service.ITransparencyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/transparencies")
public class TransparencyController extends BaseController {

    private final ITransparencyService transparencyService;

    @Autowired
    public TransparencyController(ITransparencyService transparencyService) {
        this.transparencyService = transparencyService;
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

        return handle(() -> transparencyService.uploadDocument(request), HttpStatus.CREATED);
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

        return handle(() -> transparencyService.uploadCollaborator(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<?> deleteDocument(
            @PathVariable("documentId") @NotBlank String documentId
    ) {
        return handle(() -> transparencyService.deleteDocument(UUID.fromString(documentId)), HttpStatus.OK);
    }

    @DeleteMapping("/collaborators/{collaboratorId}")
    public ResponseEntity<?> deleteCollaborator(
            @PathVariable("collaboratorId") @NotBlank String collaboratorId
    ) {
        return handle(() -> transparencyService.deleteCollaborator(UUID.fromString(collaboratorId)), HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<?> insertCategory(
            @Valid @RequestBody CreateCategoryRequestDto request
    ) {
        return handle(() -> transparencyService.insertCategory(request), HttpStatus.CREATED);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategoryById(
            @PathVariable UUID id
    ) {
        return handle(() -> transparencyService.getCategoryById(id), HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        return handle(() -> transparencyService.getAllCategories(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {

        return handle(() -> transparencyService.getAllCategoriesWithDocuments(), HttpStatus.OK);
    }

    @PatchMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCategoryDto request
    ) {
        return handle(() -> transparencyService.updateCategory(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable UUID id
    ) {
        return handle(() -> transparencyService.deleteCategory(id), HttpStatus.OK);
    }
}