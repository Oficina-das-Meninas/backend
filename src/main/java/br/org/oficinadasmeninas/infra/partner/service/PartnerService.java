package br.org.oficinadasmeninas.infra.partner.service;

import br.org.oficinadasmeninas.domain.objectstorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.partner.dto.CreatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.dto.UpdatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.repository.IPartnerRepository;
import br.org.oficinadasmeninas.domain.partner.service.IPartnerService;
import br.org.oficinadasmeninas.domain.resources.Messages;
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

    public PageDTO<Partner> findAll(String searchTerm, int page, int pageSize) {

        return partnerRepository.findByFilter(searchTerm, page, pageSize);
    }

    public Partner findById(UUID id) {

        return partnerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.PARTNER_NOT_FOUND + id));
    }

    public Partner createPartner(CreatePartnerDto request) throws IOException {
        var previewFileName = uploadMultipartFile(request.previewImage());

        var partner = new Partner();
        partner.setPreviewImageUrl(previewFileName);
        partner.setName(request.name());

        partnerRepository.insert(partner);
        return partner;
    }

    public Partner updatePartner(UUID id, UpdatePartnerDto request) throws Exception {
        var partner = partnerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.PARTNER_NOT_FOUND + id));

        var previewFileName = uploadMultipartFile(request.previewImage());

        partner.setName(request.name());
        partner.setPreviewImageUrl(previewFileName);

        partnerRepository.update(partner, request.isActive());
        return partner;
    }

    public void deletePartner(UUID id) {
        var partner = partnerRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.PARTNER_NOT_FOUND + id));

        partnerRepository.update(partner, false);
    }

    private String uploadMultipartFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty())
            return null;

        var fileName = storageService.sanitizeFileName(file.getOriginalFilename());

        storageService.upload(file, fileName, true);

        return fileName;
    }
}