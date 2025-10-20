package br.org.oficinadasmeninas.domain.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICategoriesRepository {

    Category insertCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategory(UUID id);

    boolean existsCategoryById(UUID id);

    Optional<Category> findCategoryById(UUID id);

    List<Category> findAllCategories();
}
