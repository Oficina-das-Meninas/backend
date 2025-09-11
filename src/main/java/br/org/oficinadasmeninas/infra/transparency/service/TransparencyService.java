package br.org.oficinadasmeninas.infra.transparency.service;

import br.org.oficinadasmeninas.domain.objectStorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorDto;
import br.org.oficinadasmeninas.domain.transparency.Collaborator;
import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.ResponseCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.domain.transparency.mapper.CategoryMapper;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.CategoryResponseDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.CollaboratorResponseDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.DocumentResponseDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.GetCategoriesResponseDto;
import br.org.oficinadasmeninas.domain.transparency.repository.ITransparencyRepository;
import br.org.oficinadasmeninas.domain.transparency.service.ITransparencyService;
import br.org.oficinadasmeninas.infra.transparency.exception.CollaboratorNotFoundException;
import br.org.oficinadasmeninas.infra.transparency.exception.DocumentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
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
    public void uploadDocument(
        MultipartFile file,
        String title,
        Date effectiveDate,
        String categoryId
    ) throws IOException {

        var category = transparencyRepository
                .findCategoryById(UUID.fromString(categoryId))
                .orElseThrow();

        var previewLink = objectStorage.uploadTransparencyFile(file, false);

        var dto = new CreateDocumentDto(title, category.getId(), effectiveDate, previewLink);

        transparencyRepository.insertDocument(dto);
    }

    private List<DocumentResponseDto> MapDocumentsToDto(List<Document> documents) {
        return documents.stream()
                .map(doc -> new DocumentResponseDto(
                        doc.getId(),
                        doc.getTitle(),
                        doc.getEffectiveDate(),
                        doc.getPreviewLink()
                ))
                .sorted(Comparator.comparing(DocumentResponseDto::effectiveDate).reversed())
                .toList();
    }

    @Override
    public void uploadCollaborator(
        MultipartFile file,
        String name,
        String role,
        String description,
        String priority,
        String categoryId
    ) throws IOException {

        var category = transparencyRepository
                .findCategoryById(UUID.fromString(categoryId))
                .orElseThrow();

        var imageLink = objectStorage.uploadTransparencyFile(file, true);

        Integer priorityInt;
        try {
            priorityInt = Integer.parseInt(priority);
        } catch (NumberFormatException e) {
            priorityInt = 0;
        }

        var dto = new CreateCollaboratorDto(imageLink, category.getId(), name, role, description, priorityInt);

        transparencyRepository.insertCollaborator(dto);

    }

    @Override
    public void deleteCollaborator(UUID id) throws IOException {
        var collaborator = transparencyRepository
                .findCollaboratorById(id)
                .orElseThrow(() -> new CollaboratorNotFoundException(id));

        transparencyRepository.deleteCollaborator(collaborator.getId());
        objectStorage.deleteTransparencyFile(collaborator.getImage());
    }

    @Override
    public void deleteDocument(UUID id) throws IOException {
        var document = transparencyRepository
                .findDocumentById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));

        transparencyRepository.deleteDocument(document.getId());
        objectStorage.deleteTransparencyFile(document.getPreviewLink());
    }
  
    private List<CollaboratorResponseDto> MapCollaboratorsToDto(List<Collaborator> collaborators) {
        return collaborators.stream()
                .map(c -> new CollaboratorResponseDto(
                        c.getId(),
                        c.getName(),
                        c.getRole(),
                        c.getDescription(),
                        c.getPriority(),
                        c.getImage()
                ))
                .sorted(Comparator.comparing(CollaboratorResponseDto::priority))
                .toList();
    }

    @Override
    public ResponseCategoryDto insertCategory(CreateCategoryDto request) {

        Category entity = transparencyRepository.insertCategory(CategoryMapper.toEntity(request));

        return CategoryMapper.toDto(entity);
    }

    @Override
    public ResponseCategoryDto getCategoryById(UUID id) {

        var category = transparencyRepository.findCategoryById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));

        return CategoryMapper.toDto(category);
    }

    @Override
    public List<ResponseCategoryDto> getAllCategories() {

        return transparencyRepository.findAllCategories().stream()
            .map(CategoryMapper::toDto)
            .toList();
    }

    @Override
    public GetCategoriesResponseDto getAllCategoriesWithDocuments() {

        var categories = transparencyRepository.findAllCategories();
        var documents = transparencyRepository.findAllDocuments();
        var collaborators = transparencyRepository.findAllCollaborators();

        var documentsByCategory = documents.stream()
                .collect(Collectors.groupingBy(d -> d.getCategory().getId()));

        var collaboratorsByCategory = collaborators.stream()
                .collect(Collectors.groupingBy(c -> c.getCategory().getId()));

        var categoryDtos = categories.stream()
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

        return new GetCategoriesResponseDto(categoryDtos);
    }

    @Override
    public ResponseCategoryDto updateCategory(UUID id, UpdateCategoryDto request) {

        Category existing = transparencyRepository.findCategoryById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));

        existing.setName(request.name());
        existing.setPriority(request.priority());

        Category updated = transparencyRepository.updateCategory(existing);

        return CategoryMapper.toDto(updated);
    }

    @Override
    public void deleteCategory(UUID id) {
        checkCategoryExists(id);
        checkCategoryLinks(id);

        transparencyRepository.deleteCategory(id);
    }

    private void checkCategoryExists(UUID id) {
    if (!transparencyRepository.existsCategoryById(id)) {
        throw new EntityNotFoundException("Categoria não encontrada: " + id);
    }
}

    private void checkCategoryLinks(UUID id) {
        int docs = transparencyRepository.countDocumentsByCategoryId(id);
        int collabs = transparencyRepository.countCollaboratorsByCategoryId(id);

        if (docs > 0 || collabs > 0) {
            throw new IllegalStateException(buildLinkMessage(docs, collabs));
        }
    }

    private String buildLinkMessage(int docs, int collabs) {
        StringBuilder msg = new StringBuilder("Não é possível excluir a categoria porque existem vínculos:");
        if (docs > 0) msg.append(" ").append(docs).append(" documento(s)");
        if (collabs > 0) msg.append(docs > 0 ? " e " : " ").append(collabs).append(" colaborador(es)");
        return msg.toString();
    }
}