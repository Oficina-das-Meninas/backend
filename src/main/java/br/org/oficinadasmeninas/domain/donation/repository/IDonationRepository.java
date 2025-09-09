package br.org.oficinadasmeninas.domain.donation.repository;

import br.org.oficinadasmeninas.domain.donation.Donation;

import java.util.List;
import java.util.UUID;

public interface IDonationRepository {
    Donation findById(UUID id);
    Donation save(Donation donation);
    List<Donation> findAll();
    List<Donation> findByUserId(UUID userId);
}
