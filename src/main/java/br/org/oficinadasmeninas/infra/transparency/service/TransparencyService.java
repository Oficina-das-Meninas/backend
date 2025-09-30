package br.org.oficinadasmeninas.infra.transparency.service;

import br.org.oficinadasmeninas.domain.Response;
import br.org.oficinadasmeninas.domain.objectStorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.dto.*;
import br.org.oficinadasmeninas.domain.transparency.Collaborator;
import br.org.oficinadasmeninas.domain.shared.exception.EntityNotFoundException;
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
    public Response<UUID> uploadDocument(CreateDocumentRequestDto request) throws IOException {

        var category = transparencyRepository
                .findCategoryById(UUID.fromString(request.categoryId()))
                .orElseThrow(/*TODO: Exception NOT FOUND*/);

        var previewLink = objectStorage.uploadTransparencyFile(request.file(), false);

        var dto = new CreateDocumentDto(request.title(), category.getId(), request.effectiveDate(), previewLink);

        var id = transparencyRepository.insertDocument(dto);

        return new Response<>("Documento criado com sucesso.", id);
    }

    @Override
    public Response<UUID> uploadCollaborator(CreateCollaboratorRequestDto request) throws IOException {

        var category = transparencyRepository
                .findCategoryById(UUID.fromString(request.categoryId()))
                .orElseThrow(/*TODO: Exception NOT FOUND*/);

        var imageLink = objectStorage.uploadTransparencyFile(request.file(), true);

        /*TODO: USAR MAPPER*/
        var dto = new CreateCollaboratorDto(imageLink, category.getId(), request.name(), request.role(), request.description(), 0);

        var id = transparencyRepository.insertCollaborator(dto);

        return new Response<>("Documento atualizado com sucesso.", id);
    }

    @Override
    public Response<Void> deleteCollaborator(UUID id) throws IOException {

        var collaborator = transparencyRepository
                .findCollaboratorById(id)
                .orElseThrow(() -> new CollaboratorNotFoundException(id));/*TODO: Exception NOT FOUND*/

        transparencyRepository.deleteCollaborator(collaborator.getId());
        objectStorage.deleteTransparencyFile(collaborator.getImage());

        return new Response<>("Colaborador deletado com sucesso.", null);
    }

    @Override
    public Response<Void> deleteDocument(UUID id) throws IOException {
        var document = transparencyRepository
                .findDocumentById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));

        transparencyRepository.deleteDocument(document.getId());
        objectStorage.deleteTransparencyFile(document.getPreviewLink());

        return new Response<>("Documento deletado com sucesso.", null);
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
    public Response<UUID> insertCategory(CreateCategoryRequestDto request) {

        var entity = transparencyRepository.insertCategory(CategoryMapper.toEntity(request));

        return new Response<>("Categoria criada com sucesso.", entity.getId());
    }

    @Override
    public Response<ResponseCategoryDto> getCategoryById(UUID id) {

        var category = transparencyRepository.findCategoryById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));

        return new Response<>(null, CategoryMapper.toDto(category));
    }

    @Override
    public Response<List<ResponseCategoryDto>> getAllCategories() {

        var list = transparencyRepository.findAllCategories().stream()
            .map(CategoryMapper::toDto)
            .toList();

        return new Response<>(null, list);
    }

    @Override
    public Response<GetCategoriesResponseDto> getAllCategoriesWithDocuments() {

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

        var data = new GetCategoriesResponseDto(categoryDtos);

        return new Response<>(null, data);
    }

    @Override
    public Response<Void> updateCategory(UUID id, UpdateCategoryDto request) {

        var existing = transparencyRepository.findCategoryById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));

        existing.setName(request.name());
        existing.setPriority(request.priority());

        var updated = transparencyRepository.updateCategory(existing);

        return new Response<>("Categoria atualizada com sucesso.", null);
    }

    @Override
    public Response<Void> deleteCategory(UUID id) {
        checkCategoryExists(id);
        checkCategoryLinks(id);

        transparencyRepository.deleteCategory(id);

        return new Response<>("Categoria excluida com sucesso", null);
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
}