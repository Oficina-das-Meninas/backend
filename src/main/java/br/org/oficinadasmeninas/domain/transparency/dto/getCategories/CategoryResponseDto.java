package br.org.oficinadasmeninas.domain.transparency.dto.getCategories;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResponseDto(
        UUID id,
        String name,
        Boolean isImage,
        Integer priority,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Optional<List<DocumentResponseDto>> documents,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Optional<List<CollaboratorResponseDto>> collaborators
) {
}