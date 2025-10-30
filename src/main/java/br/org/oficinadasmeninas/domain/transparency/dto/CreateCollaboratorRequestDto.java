package br.org.oficinadasmeninas.domain.transparency.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record CreateCollaboratorRequestDto(
        MultipartFile image,
        @NotBlank String name,
        @NotBlank String role,
        @NotBlank String description,
        @NotNull Integer priority,
        @NotNull UUID categoryId
) {
}