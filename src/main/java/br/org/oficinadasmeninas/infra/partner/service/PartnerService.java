package br.org.oficinadasmeninas.infra.partner.service;

import br.org.oficinadasmeninas.domain.objectstorage.IObjectStorage;
import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.partner.dto.CreatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.dto.UpdatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.repository.IPartnerRepository;
import br.org.oficinadasmeninas.domain.partner.service.IPartnerService;
import br.org.oficinadasmeninas.domain.shared.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class PartnerService implements IPartnerService{
    private final IPartnerRepository partnerRepository;
    private final IObjectStorage storageService;

    public PartnerService(IPartnerRepository partnerRepository, IObjectStorage storageService) {
        this.partnerRepository = partnerRepository;
        this.storageService = storageService;
    }

    public PageDTO<Partner> findAll(@RequestParam @PositiveOrZero int page,
                                    @RequestParam @Positive @Max(100) int pageSize){
        return partnerRepository.findAll(page, pageSize);
    }

    public Partner findById(UUID id) {

        return partnerRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patrocinador não encontrado: " + id));
    }

    public Partner createPartner(CreatePartnerDto createPartnerDto) throws IOException {
        var previewFileName = uploadMultipartFile(createPartnerDto.previewImage());

        var createdPartnerId = partnerRepository.create(createPartnerDto, previewFileName);

        return new Partner(
                createdPartnerId,
                previewFileName,
                createPartnerDto.name()
        );
    }

    public Partner updatePartner(UUID id, UpdatePartnerDto updatePartnerDto) throws Exception {
        partnerRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patrocinador não encontrado: " + id));

        var previewFileName = uploadMultipartFile(updatePartnerDto.previewImage());

        partnerRepository.update(updatePartnerDto, previewFileName);

        return new Partner(
                id,
                previewFileName,
                updatePartnerDto.name()
        );
    }

    public void deletePartner(UUID id) {
        var partner = partnerRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patrocinador não encontrado: " + id));

        partnerRepository.update(
                UpdatePartnerDto.forDeletion(partner.getId()),
                partner.getPreviewImageUrl()
        );
    }

    private String uploadMultipartFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty())
            return null;

        var fileName = storageService.sanitizeFileName(file.getOriginalFilename());

        storageService.upload(file, fileName, true);

        return fileName;
    }
}