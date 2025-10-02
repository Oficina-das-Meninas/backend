package br.org.oficinadasmeninas.infra.donation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.donation.Donation;
import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationDto;
import br.org.oficinadasmeninas.domain.donation.repository.IDonationRepository;
import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import br.org.oficinadasmeninas.domain.shared.exception.EntityNotFoundException;

@Service
public class DonationService implements IDonationService {

	private final IDonationRepository donationRepository;

	public DonationService(IDonationRepository donationRepository) {
		super();
		this.donationRepository = donationRepository;
	}

	@Override
	public List<DonationDto> getAllDonations() {
		return donationRepository.findAllDonations().stream().map(donation -> new DonationDto(donation.getId(),
				donation.getValue(), donation.getDonationAt(), donation.getUserId(), donation.getStatus())).toList();
	}

	@Override
	public DonationDto getDonationById(UUID id) {
		Donation donation = donationRepository.findDonationById(id)
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
		
		UUID id = donationRepository.createDonation(newDonation);
		
		return new DonationDto(id, donation.value(), newDonation.getDonationAt(), donation.userId(),
				donation.status());
		
	}

	@Override
	public void updateDonationStatus(UUID id, DonationStatusEnum status) {
		donationRepository.updateDonationStatus(id, status);
	}

}
