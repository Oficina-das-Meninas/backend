package br.org.oficinadasmeninas.domain.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Collaborator;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICollaboratorsRepository {

    UUID insertCollaborator(CreateCollaboratorDto request);

    void deleteCollaborator(UUID id);

    int countCollaboratorsByCategoryId(UUID id);

    Optional<Collaborator> findCollaboratorById(UUID id);

    List<Collaborator> findAllCollaborators();
}