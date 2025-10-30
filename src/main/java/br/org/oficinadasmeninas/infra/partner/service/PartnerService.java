package br.org.oficinadasmeninas.infra.partner.service;

import br.org.oficinadasmeninas.domain.objectstorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.partner.dto.CreatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.dto.UpdatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.repository.IPartnerRepository;
import br.org.oficinadasmeninas.domain.partner.service.IPartnerService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.infra.exceptions.ObjectStorageException;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class PartnerService implements IPartnerService {
    private final IPartnerRepository partnerRepository;
    private final IObjectStorage storageService;

    public PartnerService(IPartnerRepository partnerRepository, IObjectStorage storageService) {
        this.partnerRepository = partnerRepository;
        this.storageService = storageService;
    }

    public UUID insert(CreatePartnerDto request) {

        try {
            var previewFileName = uploadMultipartFile(request.previewImage());

            var partner = new Partner();
            partner.setPreviewImageUrl(previewFileName);
            partner.setName(request.name());

            partnerRepository.insert(partner);
            return partner.getId();

        } catch (IOException e) {
            throw new ObjectStorageException(e);
        }
    }

    public UUID update(UUID id, UpdatePartnerDto request) {

        var partner = partnerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.PARTNER_NOT_FOUND + id));

        try {
            var previewFileName = uploadMultipartFile(request.previewImage());

            partner.setName(request.name());
            partner.setPreviewImageUrl(previewFileName);

            partnerRepository.update(partner);
            return partner.getId();

        } catch (IOException e) {
            throw new ObjectStorageException(e);
        }
    }

    public UUID deleteById(UUID id) {
    	partnerRepository.deleteById(id);
        return id;
    }

    public PageDTO<Partner> findAll(String searchTerm, int page, int pageSize) {

        return partnerRepository.findByFilter(searchTerm, page, pageSize);
    }

    public Partner findById(UUID id) {

        return partnerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.PARTNER_NOT_FOUND + id));
    }

    private String uploadMultipartFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty())
            return null;

        var fileName = storageService.sanitizeFileName(file.getOriginalFilename());

        storageService.upload(file, fileName, true);

        return fileName;
    }
}