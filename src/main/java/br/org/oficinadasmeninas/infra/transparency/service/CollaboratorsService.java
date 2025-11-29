package br.org.oficinadasmeninas.infra.transparency.service;

import br.org.oficinadasmeninas.domain.objectstorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorRequestDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCollaboratorDto;
import br.org.oficinadasmeninas.domain.transparency.mapper.CollaboratorMapper;
import br.org.oficinadasmeninas.domain.transparency.repository.ICategoriesRepository;
import br.org.oficinadasmeninas.domain.transparency.repository.ICollaboratorsRepository;
import br.org.oficinadasmeninas.domain.transparency.service.ICollaboratorsService;
import br.org.oficinadasmeninas.infra.logging.Logging;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.exceptions.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Logging
@Service
public class CollaboratorsService implements ICollaboratorsService {

    private final ICategoriesRepository categoriesRepository;
    private final ICollaboratorsRepository collaboratorsRepository;
    private final IObjectStorage objectStorage;

    public CollaboratorsService(ICategoriesRepository categoriesRepository, ICollaboratorsRepository collaboratorsRepository, IObjectStorage objectStorage) {
        this.categoriesRepository = categoriesRepository;
        this.collaboratorsRepository = collaboratorsRepository;
        this.objectStorage = objectStorage;
    }

    @Override
    public UUID insert(CreateCollaboratorRequestDto request) {

        var category = categoriesRepository
                .findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException(Messages.CATEGORY_NOT_FOUND));

        validateImageFileType(request.image());

        var imageLink = objectStorage.uploadWithFilePath(request.image(), true);

        var collaborator = CollaboratorMapper.toEntity(request);
        collaborator.setImage(imageLink);
        collaborator.setCategory(category);

        collaboratorsRepository.insert(collaborator);
        return collaborator.getId();
    }

    @Override
    public UUID update(UUID id, UpdateCollaboratorDto request) {
        var collaborator = collaboratorsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.COLLABORATOR_NOT_FOUND));

        collaborator.setPriority(request.priority());

        collaboratorsRepository.update(collaborator);
        return collaborator.getId();
    }

    @Override
    public UUID deleteById(UUID id) {

        var collaborator = collaboratorsRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.COLLABORATOR_NOT_FOUND));

        collaboratorsRepository.deleteById(collaborator.getId());
        objectStorage.deleteFileByPath(collaborator.getImage());

        return collaborator.getId();
    }

    private void validateImageFileType(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null) {
            throw new ValidationException(Messages.FILE_NOT_IDENTIFIED);
        }

        boolean isValid = switch (contentType) {
            case "image/jpeg",
                 "image/png" -> true;
            default -> false;
        };

        if (!isValid) {
            throw new ValidationException(Messages.FILE_NOT_SUPPORTED + contentType);
        }
    }
}
