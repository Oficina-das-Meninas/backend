package br.org.oficinadasmeninas.domain.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.Collaborator;
import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITransparencyRepository {

    UUID insertDocument(CreateDocumentDto request);

    Optional<Category> findCategoryById(UUID id);

    List<Category> findAllCategories();

    List<Document> findAllDocuments();

    List<Collaborator> findAllCollaborators();
}
