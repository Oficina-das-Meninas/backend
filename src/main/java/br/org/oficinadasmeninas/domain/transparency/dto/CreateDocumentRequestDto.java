package br.org.oficinadasmeninas.domain.transparency.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

public record CreateDocumentRequestDto(
        MultipartFile file,
        @NotBlank String title,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date effectiveDate,
        UUID categoryId
) {
}
