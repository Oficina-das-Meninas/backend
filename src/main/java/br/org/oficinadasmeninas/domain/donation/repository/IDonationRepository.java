package br.org.oficinadasmeninas.domain.donation.repository;

import br.org.oficinadasmeninas.domain.donation.Donation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDonationRepository {

    Donation insert(Donation donation);

    Donation updateStatus(Donation donation);

	List<Donation> findAll();

	Optional<Donation> findById(UUID id);
}
