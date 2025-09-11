package br.org.oficinadasmeninas.infra.transparency.service;

import br.org.oficinadasmeninas.domain.objectStorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorDto;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;
import br.org.oficinadasmeninas.domain.transparency.repository.ITransparencyRepository;
import br.org.oficinadasmeninas.domain.transparency.service.ITransparencyService;
import br.org.oficinadasmeninas.infra.transparency.exception.CollaboratorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

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
}