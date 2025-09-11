package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.transparency.dto.CreateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.ResponseCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.service.ITransparencyService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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


    @PostMapping("/categories")
    public ResponseEntity<ResponseCategoryDto> insertCategory(@RequestBody CreateCategoryDto request) {
        ResponseCategoryDto response = transparencyService.insertCategory(request);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri())
                .body(response);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<ResponseCategoryDto> getCategoryById(@PathVariable UUID id) {
        ResponseCategoryDto dto = transparencyService.findCategoryById(id);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<ResponseCategoryDto>> getAllCategories() {
        List<ResponseCategoryDto> dtos = transparencyService.findAllCategories();

        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/categories/{id}")
    public ResponseEntity<ResponseCategoryDto> updateCategory(@PathVariable UUID id, @RequestBody UpdateCategoryDto request) {
        ResponseCategoryDto dto = transparencyService.updateCategory(id, request);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        transparencyService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}