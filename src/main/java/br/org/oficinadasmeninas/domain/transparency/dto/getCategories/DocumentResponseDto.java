package br.org.oficinadasmeninas.domain.transparency.dto.getCategories;

import java.util.Date;
import java.util.UUID;

public record DocumentResponseDto(
        UUID id,
        String title,
        Date effectiveDate,
        String previewLink
) {
}