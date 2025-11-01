package br.org.oficinadasmeninas.infra.donation.service;

import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationWithDonorDto;
import br.org.oficinadasmeninas.domain.donation.dto.GetDonationDto;
import br.org.oficinadasmeninas.domain.donation.mapper.DonationMapper;
import br.org.oficinadasmeninas.domain.donation.repository.IDonationRepository;
import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import br.org.oficinadasmeninas.domain.shared.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;

import static br.org.oficinadasmeninas.domain.donation.mapper.DonationMapper.toDto;
import static br.org.oficinadasmeninas.domain.donation.mapper.DonationMapper.toEntity;

@Service
public class DonationService implements IDonationService {

    private final IDonationRepository donationRepository;

    public DonationService(IDonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    @Override
    public List<DonationDto> findAll() {
        return donationRepository
                .findAll().stream()
                .map(DonationMapper::toDto)
                .toList();
    }

    public PageDTO<DonationWithDonorDto> getFilteredDonations(GetDonationDto getDonationDto){
        return donationRepository.findByFilter(getDonationDto);
    }

    @Override
    public DonationDto findById(UUID id) {
        var donation = donationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.DONATION_NOT_FOUND + id));

        return toDto(donation);
    }

    @Override
    public DonationDto insert(CreateDonationDto request) {

        var donation = toEntity(request);
        donationRepository.insert(donation);

        return toDto(donation);
    }

    @Override
    public void updateStatus(UUID id, DonationStatusEnum status) {

        var donation = donationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.DONATION_NOT_FOUND + id));

        donation.setStatus(status);
        donationRepository.updateStatus(donation);
    }
}
