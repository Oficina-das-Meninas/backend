package br.org.oficinadasmeninas.domain.sponsorship.repository;

import br.org.oficinadasmeninas.domain.sponsorship.Sponsorship;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ISponsorshipRepository {

    Sponsorship insert(Sponsorship sponsorship);

    Sponsorship update(Sponsorship sponsorship);

    void activateByUserId(UUID userId);

    void cancelById(UUID id);

    List<Sponsorship> findAll();

    Optional<Sponsorship> findById(UUID id);

    List<Sponsorship> findByUserId(UUID id);

    Optional<Sponsorship> findActiveByUserId(UUID userId);

    Optional<Sponsorship> findBySubscriptionId(String subscriptionId);
}
