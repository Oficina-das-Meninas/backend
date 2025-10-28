package br.org.oficinadasmeninas.domain.transparency.mapper;

import br.org.oficinadasmeninas.domain.transparency.Collaborator;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorRequestDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.CollaboratorResponseDto;

public class CollaboratorMapper {

    public static CollaboratorResponseDto toDto(Collaborator collaborator) {
        return new CollaboratorResponseDto(
                collaborator.getId(),
                collaborator.getName(),
                collaborator.getRole(),
                collaborator.getDescription(),
                collaborator.getPriority(),
                collaborator.getImage());
    }

    public static Collaborator toEntity(CreateCollaboratorRequestDto request) {

        var collaborator = new Collaborator();
        collaborator.setName(request.name());
        collaborator.setRole(request.role());
        collaborator.setDescription(request.description());

        return collaborator;
    }
}