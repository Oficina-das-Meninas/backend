package br.org.oficinadasmeninas.domain.donation.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.donation.Donation;
import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.donation.dto.DonationWithDonorDto;
import br.org.oficinadasmeninas.domain.donation.dto.GetDonationDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

public interface IDonationRepository {

	List<Donation> findAll();

    PageDTO<DonationWithDonorDto> getFiltered(GetDonationDto getDonationDto);

	Optional<Donation> findById(UUID id);
	
	UUID create(Donation donation);
	
	void updateStatus(UUID id, DonationStatusEnum status);
	
}
