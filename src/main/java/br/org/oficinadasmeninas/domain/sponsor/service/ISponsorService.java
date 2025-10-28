package br.org.oficinadasmeninas.domain.sponsor.service;

import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.dto.UpdateSponsorDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ISponsorService {

    UUID insert(SponsorDto sponsor);

    void update(UpdateSponsorDto sponsor);

    void activateByUserId(UUID userId);

    Optional<SponsorDto> findById(UUID id);

    List<SponsorDto> findByUserId(UUID id);

    Optional<SponsorDto> findActiveByUserId(UUID id);

    Optional<SponsorDto> findBySubscriptionId(UUID subscriptionId);
}
