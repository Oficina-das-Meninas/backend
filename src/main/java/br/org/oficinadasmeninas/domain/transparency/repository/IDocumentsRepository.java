package br.org.oficinadasmeninas.domain.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDocumentsRepository {

    UUID insertDocument(CreateDocumentDto request);

    void deleteDocument(UUID id);

    int countDocumentsByCategoryId(UUID id);

    Optional<Document> findDocumentById(UUID id);

    List<Document> findAllDocuments();
}
