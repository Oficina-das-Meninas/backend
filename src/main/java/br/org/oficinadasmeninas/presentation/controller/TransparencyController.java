package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.transparency.service.ITransparencyService;
import br.org.oficinadasmeninas.infra.transparency.exception.CollaboratorNotFoundException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
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

    @DeleteMapping("/collaborators/{collaboratorId}")
    public ResponseEntity<String> uploadCollaborator(
            @PathVariable("collaboratorId") @NotBlank String collaboratorId
    ) throws IOException {

        try{
            transparencyService.deleteCollaborator(UUID.fromString(collaboratorId));
        }catch (CollaboratorNotFoundException c){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Colaborador deletado com sucesso!");
    }
}