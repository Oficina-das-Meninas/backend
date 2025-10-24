package br.org.oficinadasmeninas.infra.transparency.service;

import br.org.oficinadasmeninas.domain.objectstorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.shared.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorDto;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorRequestDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCollaboratorDto;
import br.org.oficinadasmeninas.domain.transparency.repository.ICategoriesRepository;
import br.org.oficinadasmeninas.domain.transparency.repository.ICollaboratorsRepository;
import br.org.oficinadasmeninas.domain.transparency.service.ICollaboratorsService;
import br.org.oficinadasmeninas.infra.exceptions.ObjectStorageException;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class CollaboratorsService implements ICollaboratorsService {

    private final ICategoriesRepository  categoriesRepository;
    private final ICollaboratorsRepository   collaboratorsRepository;
    private final IObjectStorage objectStorage;

    @Autowired
    public CollaboratorsService(ICategoriesRepository categoriesRepository, ICollaboratorsRepository collaboratorsRepository, IObjectStorage objectStorage) {
        this.categoriesRepository = categoriesRepository;
        this.collaboratorsRepository = collaboratorsRepository;
        this.objectStorage = objectStorage;
    }

    @Override
    public UUID uploadCollaborator(CreateCollaboratorRequestDto request) {

        var category = categoriesRepository
                .findById(UUID.fromString(request.categoryId()))
                .orElseThrow(() -> new NotFoundException(Messages.CATEGORY_NOT_FOUND));
        try {
            var imageLink = objectStorage.uploadTransparencyFile(request.file(), true);

            /*TODO: USAR MAPPER*/
            var dto = new CreateCollaboratorDto(imageLink, category.getId(), request.name(), request.role(), request.description(), 0);

            var id = collaboratorsRepository.insert(dto);

            return id;

        } catch (IOException e) {
            throw new ObjectStorageException(e);
        }
    }

    @Override
    public UUID deleteCollaborator(UUID id) {

        var collaborator = collaboratorsRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.COLLABORATOR_NOT_FOUND));

        try {
            collaboratorsRepository.delete(collaborator.getId());
            objectStorage.deleteTransparencyFile(collaborator.getImage());

            return collaborator.getId();
        } catch (IOException e) {
            throw new ObjectStorageException(e);
        }
    }

    @Override
    public UUID updateCollaborator(UUID id, UpdateCollaboratorDto request) {
        var existing = collaboratorsRepository.findCollaboratorById(id)
                .orElseThrow(() -> new NotFoundException(Messages.COLLABORATOR_NOT_FOUND));

        existing.setPriority(request.priority());

        var updated = collaboratorsRepository.updateCollaborator(existing);

        return updated.getId();
    }
}
