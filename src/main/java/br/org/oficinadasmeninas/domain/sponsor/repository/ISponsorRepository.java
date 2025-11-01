package br.org.oficinadasmeninas.domain.sponsor.repository;

import br.org.oficinadasmeninas.domain.sponsor.Sponsor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ISponsorRepository {

    Sponsor insert(Sponsor sponsor);

    Sponsor update(Sponsor sponsor);

    void activateByUserId(UUID userId);

    void suspendById(UUID id);

    List<Sponsor> findAll();

    Optional<Sponsor> findById(UUID id);

    List<Sponsor> findByUserId(UUID id);

    Optional<Sponsor> findActiveByUserId(UUID userId);

    Optional<Sponsor> findBySubscriptionId(UUID subscriptionId);
}
