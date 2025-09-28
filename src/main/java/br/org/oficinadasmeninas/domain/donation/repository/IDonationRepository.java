package br.org.oficinadasmeninas.domain.donation.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.donation.Donation;
import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;

public interface IDonationRepository {

	List<Donation> findAllDonations();

	Optional<Donation> findDonationById(UUID id);
	
	UUID createDonation(Donation donation);
	
	void updateDonationStatus(UUID id, DonationStatusEnum status);
	
}
