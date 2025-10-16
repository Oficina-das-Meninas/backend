package br.org.oficinadasmeninas.domain.transparency.service;

import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentRequestDto;

import java.util.UUID;

public interface IDocumentsService {

    UUID uploadDocument(CreateDocumentRequestDto request);

    UUID deleteDocument(UUID id);
}