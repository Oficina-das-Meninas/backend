package br.org.oficinadasmeninas.infra.partner.service;

import br.org.oficinadasmeninas.domain.objectstorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.partner.dto.CreatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.dto.UpdatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.repository.IPartnerRepository;
import br.org.oficinadasmeninas.domain.partner.service.IPartnerService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.infra.logging.Logging;
import br.org.oficinadasmeninas.infra.objectstorage.rollback.MinIoRollbackContext;
import br.org.oficinadasmeninas.infra.objectstorage.rollback.MinIoTransactional;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Logging
@Service
public class PartnerService implements IPartnerService {
    private final IPartnerRepository partnerRepository;
    private final IObjectStorage storageService;
    private final MinIoRollbackContext minIoRollbackContext;

    public PartnerService(IPartnerRepository partnerRepository, IObjectStorage storageService, MinIoRollbackContext minIoRollbackContext) {
        this.partnerRepository = partnerRepository;
        this.storageService = storageService;
        this.minIoRollbackContext = minIoRollbackContext;
    }

    @Override
    @Transactional
    @MinIoTransactional
    public UUID insert(CreatePartnerDto request) {

        var previewFileName = uploadMultipartFile(request.previewImage());
        minIoRollbackContext.register(previewFileName);

        var partner = new Partner();
        partner.setPreviewImageUrl(previewFileName);
        partner.setName(request.name());

        partnerRepository.insert(partner);
        return partner.getId();
    }

    @Override
    @Transactional
    @MinIoTransactional
    public UUID update(UUID id, UpdatePartnerDto request) {

        var partner = partnerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.PARTNER_NOT_FOUND + id));

        var previewFileName = uploadMultipartFile(request.previewImage());
        minIoRollbackContext.register(previewFileName);

        partner.setName(request.name());
        partner.setPreviewImageUrl(previewFileName);

        partnerRepository.update(partner);
        return partner.getId();
    }

    @Override
    @Transactional
    public UUID deleteById(UUID id) {
        partnerRepository.deleteById(id);
        return id;
    }

    @Override
    public PageDTO<Partner> findAll(String searchTerm, int page, int pageSize) {

        return partnerRepository.findByFilter(searchTerm, page, pageSize);
    }

    @Override
    public Partner findById(UUID id) {

        return partnerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.PARTNER_NOT_FOUND + id));
    }

    private String uploadMultipartFile(MultipartFile file) {
        if (file == null || file.isEmpty())
            return null;

        return storageService.uploadFile(file, true);
    }
}