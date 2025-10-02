package br.org.oficinadasmeninas.domain.donation.service;

import java.util.List;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationDto;

public interface IDonationService {

	List<DonationDto> getAllDonations();

	DonationDto getDonationById(UUID id);
	
	DonationDto createDonation(CreateDonationDto donation);
	
	void updateDonationStatus(UUID id, DonationStatusEnum status);
	
}
