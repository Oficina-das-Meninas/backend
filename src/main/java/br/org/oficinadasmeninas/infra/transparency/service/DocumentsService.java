package br.org.oficinadasmeninas.infra.transparency.service;

import br.org.oficinadasmeninas.domain.objectstorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentRequestDto;
import br.org.oficinadasmeninas.domain.transparency.repository.ICategoriesRepository;
import br.org.oficinadasmeninas.domain.transparency.repository.IDocumentsRepository;
import br.org.oficinadasmeninas.domain.transparency.service.IDocumentsService;
import br.org.oficinadasmeninas.infra.logging.Logging;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static br.org.oficinadasmeninas.domain.transparency.mapper.DocumentMapper.toEntity;

@Logging
@Service
public class DocumentsService implements IDocumentsService {

    private final IObjectStorage objectStorage;
    private final ICategoriesRepository categoriesRepository;
    private final IDocumentsRepository documentsRepository;

    public DocumentsService(IObjectStorage objectStorage, ICategoriesRepository categoriesRepository, IDocumentsRepository documentsRepository) {
        this.objectStorage = objectStorage;
        this.categoriesRepository = categoriesRepository;
        this.documentsRepository = documentsRepository;
    }

    @Override
    public UUID create(CreateDocumentRequestDto request) {

        var category = categoriesRepository
                .findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException(Messages.CATEGORY_NOT_FOUND));

        var previewLink = objectStorage.uploadWithFilePath(request.file(), false);

        var document = toEntity(request);
        document.setCategory(category);
        document.setPreviewLink(previewLink);

        documentsRepository.insert(document);
        return document.getId();
    }

    @Override
    public UUID deleteById(UUID id) {
        var document = documentsRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.DOCUMENT_NOT_FOUND));

        documentsRepository.deleteById(document.getId());
        objectStorage.deleteFileByPath(document.getPreviewLink());

        return document.getId();
    }
}