package br.org.oficinadasmeninas.infra.transparency.service;

import br.org.oficinadasmeninas.domain.objectStorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.transparency.Collaborator;
import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.CategoryResponseDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.CollaboratorResponseDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.DocumentResponseDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.GetCategoriesResponseDto;
import br.org.oficinadasmeninas.domain.transparency.repository.ITransparencyRepository;
import br.org.oficinadasmeninas.domain.transparency.service.ITransparencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
}