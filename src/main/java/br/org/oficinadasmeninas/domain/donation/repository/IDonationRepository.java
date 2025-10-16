package br.org.oficinadasmeninas.domain.donation.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.donation.Donation;
import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;

public interface IDonationRepository {

	List<Donation> findAll();

	Optional<Donation> findById(UUID id);
	
	UUID create(Donation donation);
	
	void updateStatus(UUID id, DonationStatusEnum status);
	
}
