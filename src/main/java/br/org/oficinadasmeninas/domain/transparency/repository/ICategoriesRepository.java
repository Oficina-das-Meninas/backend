package br.org.oficinadasmeninas.domain.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICategoriesRepository {

    Category insert(Category category);

    Category update(Category category);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    Optional<Category> findById(UUID id);

    List<Category> findAll();
}
