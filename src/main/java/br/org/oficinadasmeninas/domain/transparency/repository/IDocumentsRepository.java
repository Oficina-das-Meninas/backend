package br.org.oficinadasmeninas.domain.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDocumentsRepository {

    Document insert(Document document);

    void deleteById(UUID id);

    List<Document> findAll();

    Optional<Document> findById(UUID id);

    int countByCategoryId(UUID categoryId);
}
