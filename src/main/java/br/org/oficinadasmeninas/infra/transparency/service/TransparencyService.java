package br.org.oficinadasmeninas.infra.transparency.service;

import br.org.oficinadasmeninas.domain.objectStorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.dto.*;
import br.org.oficinadasmeninas.domain.transparency.Collaborator;
import br.org.oficinadasmeninas.domain.shared.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.domain.transparency.mapper.CategoryMapper;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.CategoryResponseDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.CollaboratorResponseDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.DocumentResponseDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.GetCategoriesResponseDto;
import br.org.oficinadasmeninas.domain.transparency.mapper.CollaboratorMapper;
import br.org.oficinadasmeninas.domain.transparency.mapper.DocumentMapper;
import br.org.oficinadasmeninas.domain.transparency.repository.ITransparencyRepository;
import br.org.oficinadasmeninas.domain.transparency.service.ITransparencyService;
import br.org.oficinadasmeninas.infra.exceptions.ObjectStorageException;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransparencyService implements ITransparencyService {

    private final IObjectStorage objectStorage;
    private final ITransparencyRepository transparencyRepository;

    @Autowired
    public TransparencyService(IObjectStorage objectStorage, ITransparencyRepository transparencyRepository) {
        this.objectStorage = objectStorage;
        this.transparencyRepository = transparencyRepository;
    }

    @Override
    public UUID insertCategory(CreateCategoryRequestDto request) {

        var entity = transparencyRepository.insertCategory(CategoryMapper.toEntity(request));

        return entity.getId();
    }

    @Override
    public UUID uploadDocument(CreateDocumentRequestDto request) {

        var category = transparencyRepository
                .findCategoryById(UUID.fromString(request.categoryId()))
                .orElseThrow(() -> new NotFoundException(Messages.CATEGORY_NOT_FOUND));

        try {
            var previewLink = objectStorage.uploadTransparencyFile(request.file(), false);

            var dto = new CreateDocumentDto(request.title(), category.getId(), request.effectiveDate(), previewLink);

            return transparencyRepository.insertDocument(dto);

        } catch (IOException e) {
            throw new ObjectStorageException(e);
        }
    }

    @Override
    public UUID uploadCollaborator(CreateCollaboratorRequestDto request) {

        var category = transparencyRepository
                .findCategoryById(UUID.fromString(request.categoryId()))
                .orElseThrow(() -> new NotFoundException(Messages.CATEGORY_NOT_FOUND));
        try {
            var imageLink = objectStorage.uploadTransparencyFile(request.file(), true);

            /*TODO: USAR MAPPER*/
            var dto = new CreateCollaboratorDto(imageLink, category.getId(), request.name(), request.role(), request.description(), 0);

            var id = transparencyRepository.insertCollaborator(dto);

            return id;

        } catch (IOException e) {
            throw new ObjectStorageException(e);
        }
    }

    @Override
    public UUID updateCategory(UUID id, UpdateCategoryDto request) {

        var existing = transparencyRepository.findCategoryById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));

        existing.setName(request.name());
        existing.setPriority(request.priority());

        var updated = transparencyRepository.updateCategory(existing);

        return updated.getId();
    }

    @Override
    public UUID deleteCategory(UUID id) {
        checkCategoryExists(id);
        checkCategoryLinks(id);

        transparencyRepository.deleteCategory(id);

        return id;
    }

    @Override
    public UUID deleteDocument(UUID id) {
        var document = transparencyRepository
                .findDocumentById(id)
                .orElseThrow(() -> new NotFoundException(Messages.DOCUMENT_NOT_FOUND));

        try {
            transparencyRepository.deleteDocument(document.getId());
            objectStorage.deleteTransparencyFile(document.getPreviewLink());

            return document.getId();
        } catch (IOException e) {
            throw new ObjectStorageException(e);
        }
    }

    @Override
    public UUID deleteCollaborator(UUID id) {

        var collaborator = transparencyRepository
                .findCollaboratorById(id)
                .orElseThrow(() -> new NotFoundException(Messages.COLLABORATOR_NOT_FOUND));

        try {
            transparencyRepository.deleteCollaborator(collaborator.getId());
            objectStorage.deleteTransparencyFile(collaborator.getImage());

            return collaborator.getId();
        } catch (IOException e) {
            throw new ObjectStorageException(e);
        }
    }

    @Override
    public ResponseCategoryDto getCategoryById(UUID id) {

        var category = transparencyRepository.findCategoryById(id)
                .orElseThrow(() -> new NotFoundException(Messages.CATEGORY_NOT_FOUND));

        return CategoryMapper.toDto(category);
    }

    @Override
    public List<ResponseCategoryDto> getAllCategories() {

        return transparencyRepository.findAllCategories()
                .stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    @Override
    public GetCategoriesResponseDto getAllCategoriesWithDocuments() {

        var categories = transparencyRepository.findAllCategories();
        var documents = transparencyRepository.findAllDocuments();
        var collaborators = transparencyRepository.findAllCollaborators();

        return new GetCategoriesResponseDto(mapCategoriesToDto(categories, documents, collaborators));
    }

    private List<CategoryResponseDto> mapCategoriesToDto(List<Category> categories, List<Document> documents, List<Collaborator> collaborators) {

        var documentsByCategory = documents.stream()
                .collect(Collectors.groupingBy(d -> d.getCategory().getId()));

        var collaboratorsByCategory = collaborators.stream()
                .collect(Collectors.groupingBy(c -> c.getCategory().getId()));

        return categories
                .stream()
                .map(cat -> new CategoryResponseDto(
                        cat.getId(),
                        cat.getName(),
                        cat.getImage(),
                        cat.getPriority(),
                        Optional.ofNullable(documentsByCategory.get(cat.getId()))
                                .map(this::MapDocumentsToDto),
                        Optional.ofNullable(collaboratorsByCategory.get(cat.getId()))
                                .map(this::MapCollaboratorsToDto)
                ))
                .sorted(Comparator.comparing(CategoryResponseDto::priority))
                .toList();
    }

    private List<CollaboratorResponseDto> MapCollaboratorsToDto(List<Collaborator> collaborators) {
        return collaborators.stream()
                .map(CollaboratorMapper::toDto)
                .sorted(Comparator.comparing(CollaboratorResponseDto::priority))
                .toList();
    }

    private List<DocumentResponseDto> MapDocumentsToDto(List<Document> documents) {
        return documents.stream()
                .map(DocumentMapper::toDto)
                .sorted(Comparator.comparing(DocumentResponseDto::effectiveDate).reversed())
                .toList();
    }

    private void checkCategoryExists(UUID id) {
        if (!transparencyRepository.existsCategoryById(id)) {
            throw new NotFoundException(Messages.CATEGORY_NOT_FOUND);
        }
    }

    private void checkCategoryLinks(UUID id) {
        int docs = transparencyRepository.countDocumentsByCategoryId(id);
        int collabs = transparencyRepository.countCollaboratorsByCategoryId(id);

        if (docs > 0 || collabs > 0) {
            throw new ValidationException(buildLinkMessage(docs, collabs));
        }
    }

    private String buildLinkMessage(int docs, int collabs) {
        var msg = new StringBuilder("Não é possível excluir a categoria porque existem vínculos:");

        if (docs > 0)
            msg.append(" ").append(docs).append(" documento(s)");

        if (collabs > 0)
            msg.append(docs > 0 ? " e " : " ").append(collabs).append(" colaborador(es)");

        return msg.toString();
    }
}