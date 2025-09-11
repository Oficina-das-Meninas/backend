package br.org.oficinadasmeninas.domain.transparency.dto;

import java.util.Date;
import java.util.UUID;

public record CreateDocumentDto(
        String title,
        UUID categoryId,
        Date effectiveDate,
        String previewLink
) { }