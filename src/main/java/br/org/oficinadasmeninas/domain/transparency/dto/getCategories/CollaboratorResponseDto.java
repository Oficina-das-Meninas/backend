package br.org.oficinadasmeninas.domain.transparency.dto.getCategories;

import java.util.UUID;

public record CollaboratorResponseDto(
        UUID id,
        String name,
        String role,
        String description,
        Integer priority,
        String previewLink
) {
}
