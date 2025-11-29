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
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PartnerService implements IPartnerService {
    private final IPartnerRepository partnerRepository;
    private final IObjectStorage storageService;

    public PartnerService(IPartnerRepository partnerRepository, IObjectStorage storageService) {
        this.partnerRepository = partnerRepository;
        this.storageService = storageService;
    }

    @Transactional
    public UUID insert(CreatePartnerDto request) {
        var previewImageUrl = storageService.uploadWithFilePath(request.previewImage(), true);

        var partner = new Partner();
        partner.setPreviewImageUrl(previewImageUrl);
        partner.setName(request.name());

        partnerRepository.insert(partner);

        return partner.getId();
    }

    @Transactional
    public UUID update(UUID id, UpdatePartnerDto request) {
        var partner = partnerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.PARTNER_NOT_FOUND + id));

        var previewImageUrl = storageService.uploadWithFilePath(request.previewImage(), true);

        partner.setName(request.name());
        partner.setPreviewImageUrl(previewImageUrl);

        partnerRepository.update(partner);
        return partner.getId();
    }

    @Transactional
    public UUID deleteById(UUID id) {
        var partner = findById(id);

    	partnerRepository.deleteById(partner.getId());
        storageService.deleteFileByPath(partner.getPreviewImageUrl());

        return partner.getId();
    }

    public PageDTO<Partner> findAll(String searchTerm, int page, int pageSize) {
        return partnerRepository.findByFilter(searchTerm, page, pageSize);
    }

    public Partner findById(UUID id) {
        return partnerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.PARTNER_NOT_FOUND + id));
    }
}