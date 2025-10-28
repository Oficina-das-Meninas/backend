package br.org.oficinadasmeninas.infra.donation.service;

import br.org.oficinadasmeninas.domain.donation.Donation;
import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationWithDonorDto;
import br.org.oficinadasmeninas.domain.donation.dto.GetDonationDto;
import br.org.oficinadasmeninas.domain.donation.repository.IDonationRepository;
import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import br.org.oficinadasmeninas.domain.shared.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DonationService implements IDonationService {

	private final IDonationRepository donationRepository;

	public DonationService(IDonationRepository donationRepository) {
		super();
		this.donationRepository = donationRepository;
	}

	@Override
	public List<DonationDto> getAllDonations() {
		return donationRepository.findAll().stream().map(donation -> new DonationDto(donation.getId(),
				donation.getValue(), donation.getDonationAt(), donation.getUserId(), donation.getStatus())).toList();
	}

    public PageDTO<DonationWithDonorDto> getFilteredDonations(GetDonationDto getDonationDto){
        return donationRepository.getFiltered(getDonationDto);
    }

	@Override
	public DonationDto getDonationById(UUID id) {
		Donation donation = donationRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Doação não encontrada com id: " + id));

		return new DonationDto(donation.getId(), donation.getValue(), donation.getDonationAt(), donation.getUserId(),
				donation.getStatus());
	}

	@Override
	public DonationDto createDonation(CreateDonationDto donation) {
		Donation newDonation = new Donation();
		newDonation.setValue(donation.value());
		newDonation.setStatus(donation.status());
		newDonation.setDonationAt(LocalDateTime.now());
		newDonation.setUserId(donation.userId());
		
		UUID id = donationRepository.create(newDonation);
		
		return new DonationDto(id, donation.value(), newDonation.getDonationAt(), donation.userId(),
				donation.status());
		
	}

	@Override
	public void updateDonationStatus(UUID id, DonationStatusEnum status) {
		donationRepository.updateStatus(id, status);
	}

}
