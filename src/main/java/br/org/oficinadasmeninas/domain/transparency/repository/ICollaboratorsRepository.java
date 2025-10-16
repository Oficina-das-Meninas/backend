package br.org.oficinadasmeninas.domain.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Collaborator;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICollaboratorsRepository {

    UUID insert(CreateCollaboratorDto request);

    void delete(UUID id);

    int countByCategoryId(UUID id);

    Optional<Collaborator> findById(UUID id);

    List<Collaborator> findAll();
}