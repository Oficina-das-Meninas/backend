package br.org.oficinadasmeninas.domain.transparency.mapper;

import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentRequestDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.DocumentResponseDto;

public class DocumentMapper {

    public static DocumentResponseDto toDto(Document document) {
        return new DocumentResponseDto(
                document.getId(),
                document.getTitle(),
                document.getEffectiveDate(),
                document.getPreviewLink());
    }

    public static Document toEntity(CreateDocumentRequestDto request){
        var document = new Document();

        document.setTitle(request.title());
        document.setEffectiveDate(request.effectiveDate());

        return document;
    }
}