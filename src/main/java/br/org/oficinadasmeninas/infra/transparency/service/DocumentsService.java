package br.org.oficinadasmeninas.infra.transparency.service;

import br.org.oficinadasmeninas.domain.objectStorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentRequestDto;
import br.org.oficinadasmeninas.domain.transparency.repository.ICategoriesRepository;
import br.org.oficinadasmeninas.domain.transparency.repository.IDocumentsRepository;
import br.org.oficinadasmeninas.domain.transparency.service.IDocumentsService;
import br.org.oficinadasmeninas.infra.exceptions.ObjectStorageException;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class DocumentsService implements IDocumentsService {

    private final IObjectStorage objectStorage;
    private final ICategoriesRepository categoriesRepository;
    private final IDocumentsRepository documentsRepository;

    @Autowired
    public DocumentsService(IObjectStorage objectStorage, ICategoriesRepository categoriesRepository, IDocumentsRepository documentsRepository) {
        this.objectStorage = objectStorage;
        this.categoriesRepository = categoriesRepository;
        this.documentsRepository = documentsRepository;
    }

    @Override
    public UUID uploadDocument(CreateDocumentRequestDto request) {

        var category = categoriesRepository
                .findById(UUID.fromString(request.categoryId()))
                .orElseThrow(() -> new NotFoundException(Messages.CATEGORY_NOT_FOUND));

        try {
            var previewLink = objectStorage.uploadTransparencyFile(request.file(), false);

            var dto = new CreateDocumentDto(request.title(), category.getId(), request.effectiveDate(), previewLink);

            return documentsRepository.insert(dto);

        } catch (IOException e) {
            throw new ObjectStorageException(e);
        }
    }

    @Override
    public UUID deleteDocument(UUID id) {
        var document = documentsRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.DOCUMENT_NOT_FOUND));

        try {
            documentsRepository.delete(document.getId());
            objectStorage.deleteTransparencyFile(document.getPreviewLink());

            return document.getId();
        } catch (IOException e) {
            throw new ObjectStorageException(e);
        }
    }
}