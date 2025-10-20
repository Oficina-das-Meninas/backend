package br.org.oficinadasmeninas.domain.transparency.mapper;

import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.DocumentResponseDto;

public class DocumentMapper {

    public static DocumentResponseDto toDto(Document document) {
        return new DocumentResponseDto(
                document.getId(),
                document.getTitle(),
                document.getEffectiveDate(),
                document.getPreviewLink());
    }
}