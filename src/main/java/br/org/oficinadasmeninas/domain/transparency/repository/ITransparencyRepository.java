package br.org.oficinadasmeninas.domain.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.Collaborator;
import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorDto;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITransparencyRepository {

    UUID insertDocument(CreateDocumentDto request);
    UUID insertCollaborator(CreateCollaboratorDto request);
    Category insertCategory(Category category);

    Optional<Category> findCategoryById(UUID id);

    Category updateCategory(Category category);
    void deleteCategory(UUID id);

    boolean existsCategoryById(UUID id);
    int countDocumentsByCategoryId(UUID id);
    int countCollaboratorsByCategoryId(UUID id);

    List<Category> findAllCategories();
    List<Document> findAllDocuments();
    List<Collaborator> findAllCollaborators();
}
