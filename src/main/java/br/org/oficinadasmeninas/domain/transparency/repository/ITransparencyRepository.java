package br.org.oficinadasmeninas.domain.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITransparencyRepository {

    UUID insertDocument(CreateDocumentDto request);

    Category insertCategory(Category category);
    Optional<Category> findCategoryById(UUID id);
    List<Category> findAllCategories();
    Category updateCategory(Category category);
    void deleteCategory(UUID id);

    boolean existsCategoryById(UUID id);
    int countDocumentsByCategoryId(UUID id);
    int countCollaboratorsByCategoryId(UUID id);
}
