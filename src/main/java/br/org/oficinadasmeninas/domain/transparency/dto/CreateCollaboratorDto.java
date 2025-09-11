package br.org.oficinadasmeninas.domain.transparency.dto;

import br.org.oficinadasmeninas.domain.transparency.Category;

import java.util.UUID;

public record CreateCollaboratorDto(
        String image,
        UUID categoryId,
        String name,
        String role,
        String description,
        Integer priority
) {
}
