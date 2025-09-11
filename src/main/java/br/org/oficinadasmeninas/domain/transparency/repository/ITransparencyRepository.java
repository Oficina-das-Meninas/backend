package br.org.oficinadasmeninas.domain.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.Collaborator;
import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorDto;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;

import java.util.Optional;
import java.util.UUID;

public interface ITransparencyRepository {

    UUID insertDocument(CreateDocumentDto request);

    UUID insertCollaborator(CreateCollaboratorDto request);

    Optional<Category> findCategoryById(UUID id);

    Optional<Collaborator> findCollaboratorById(UUID id);

    Optional<Document> findDocumentById(UUID id);

    void deleteCollaborator(UUID id);

    void deleteDocument(UUID id);
}
