package br.org.oficinadasmeninas.domain.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDocumentsRepository {

    UUID insert(CreateDocumentDto request);

    void delete(UUID id);

    int countByCategoryId(UUID id);

    Optional<Document> findById(UUID id);

    List<Document> findAll();
}
