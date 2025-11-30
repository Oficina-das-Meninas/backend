package br.org.oficinadasmeninas.infra.transparency.service;

import br.org.oficinadasmeninas.domain.objectstorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentRequestDto;
import br.org.oficinadasmeninas.domain.transparency.repository.ICategoriesRepository;
import br.org.oficinadasmeninas.domain.transparency.repository.IDocumentsRepository;
import br.org.oficinadasmeninas.domain.transparency.service.IDocumentsService;
import br.org.oficinadasmeninas.infra.logging.Logging;
import br.org.oficinadasmeninas.infra.objectstorage.rollback.MinIoRollbackContext;
import br.org.oficinadasmeninas.infra.objectstorage.rollback.MinIoTransactional;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static br.org.oficinadasmeninas.domain.transparency.mapper.DocumentMapper.toEntity;

@Logging
@Service
public class DocumentsService implements IDocumentsService {

    private final ICategoriesRepository categoriesRepository;
    private final IDocumentsRepository documentsRepository;
    private final IObjectStorage objectStorage;
    private final MinIoRollbackContext minIoRollbackContext;

    public DocumentsService(
            ICategoriesRepository categoriesRepository,
            IDocumentsRepository documentsRepository,
            IObjectStorage objectStorage,
            MinIoRollbackContext minIoRollbackContext
    ) {
        this.categoriesRepository = categoriesRepository;
        this.documentsRepository = documentsRepository;
        this.objectStorage = objectStorage;
        this.minIoRollbackContext = minIoRollbackContext;
    }

    @Override
    @Transactional
    @MinIoTransactional
    public UUID create(CreateDocumentRequestDto request) {

        var category = categoriesRepository
                .findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException(Messages.CATEGORY_NOT_FOUND));

        var previewLink = objectStorage.uploadTransparencyFile(request.file(), false);
        minIoRollbackContext.register(previewLink);

        var document = toEntity(request);
        document.setCategory(category);
        document.setPreviewLink(previewLink);

        documentsRepository.insert(document);
        return document.getId();
    }

    @Override
    @Transactional
    public UUID deleteById(UUID id) {
        var document = documentsRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.DOCUMENT_NOT_FOUND));

        documentsRepository.deleteById(document.getId());
        objectStorage.deleteFile(document.getPreviewLink());

        return document.getId();
    }
}