package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.infra.transparency.exception.CollaboratorNotFoundException;
import br.org.oficinadasmeninas.infra.transparency.exception.DocumentNotFoundException;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.ResponseCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.GetCategoriesResponseDto;
import br.org.oficinadasmeninas.domain.transparency.service.ITransparencyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/transparencies")
public class TransparencyController {

    private final ITransparencyService transparencyService;

    @Autowired
    public TransparencyController(ITransparencyService transparencyService) {
        this.transparencyService = transparencyService;
    }

    @PostMapping("/documents")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") @NotBlank String title,
            @RequestParam("effectiveDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date effectiveDate,
            @RequestParam("categoryId") @NotBlank String categoryId
    ) throws IOException {

        transparencyService.uploadDocument(file, title, effectiveDate, categoryId);

        return ResponseEntity.ok("Arquivo enviado com sucesso!");
    }

    @PostMapping("/collaborators")
    public ResponseEntity<String> uploadCollaborator(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") @NotBlank String name,
            @RequestParam("role") String role,
            @RequestParam("description") String description,
            @RequestParam("priority") @NotBlank String priority,
            @RequestParam("categoryId") @NotBlank String categoryId
    ) throws IOException {

        transparencyService.uploadCollaborator(image, name, role, description, priority, categoryId);

        return ResponseEntity.ok("Colaborador enviado com sucesso!");
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<String> deleteDocument(
            @PathVariable("documentId") @NotBlank String documentId
    ) throws IOException {

        try{
            transparencyService.deleteDocument(UUID.fromString(documentId));
        }catch (DocumentNotFoundException d){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Documento deletado com sucesso!");
    }

    @DeleteMapping("/collaborators/{collaboratorId}")
    public ResponseEntity<String> deleteCollaborator(
            @PathVariable("collaboratorId") @NotBlank String collaboratorId
    ) throws IOException {

        try{
            transparencyService.deleteCollaborator(UUID.fromString(collaboratorId));
        }catch (CollaboratorNotFoundException c){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Colaborador deletado com sucesso!");

    @PostMapping("/categories")
    public ResponseEntity<ResponseCategoryDto> insertCategory(@Valid @RequestBody CreateCategoryDto request) {
        ResponseCategoryDto response = transparencyService.insertCategory(request);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri())
                .body(response);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<ResponseCategoryDto> getCategoryById(@PathVariable UUID id) {
        ResponseCategoryDto dto = transparencyService.getCategoryById(id);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<ResponseCategoryDto>> getAllCategories() {
        List<ResponseCategoryDto> dtos = transparencyService.getAllCategories();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    public ResponseEntity<GetCategoriesResponseDto> getAll() {
        var response = transparencyService
                .getAllCategoriesWithDocuments();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/categories/{id}")
    public ResponseEntity<ResponseCategoryDto> updateCategory(@PathVariable UUID id, @Valid @RequestBody UpdateCategoryDto request) {
        ResponseCategoryDto dto = transparencyService.updateCategory(id, request);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        transparencyService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}