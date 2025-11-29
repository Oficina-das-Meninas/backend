package br.org.oficinadasmeninas.domain.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Collaborator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICollaboratorsRepository {

    Collaborator insert(Collaborator collaborator);

    Collaborator update(Collaborator collaborator);

    void deleteById(UUID id);

    List<Collaborator> findAll();

    Optional<Collaborator> findById(UUID id);

    int countByCategoryId(UUID categoryId);
}