package br.org.oficinadasmeninas.domain.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;

import java.util.List;
import java.util.UUID;

public interface ICategoryRepository {
    Category save(Category category);
    Category update(Category category);
    void delete(UUID id);
    Category findById(UUID id);
    List<Category> findAll();
}
