package br.org.oficinadasmeninas.domain.transparency.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public record CreateDocumentRequestDto(
        MultipartFile file,
        String title,
        Date effectiveDate,
        String categoryId
) { }
