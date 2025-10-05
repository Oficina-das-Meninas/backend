package br.org.oficinadasmeninas.domain.sponsor.repository;

import br.org.oficinadasmeninas.domain.sponsor.Sponsor;
import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.dto.UpdateSponsorDto;
import br.org.oficinadasmeninas.domain.user.User;

import java.util.Optional;
import java.util.UUID;

public interface ISponsorRepository {
    Optional<SponsorDto> findById(UUID id);
    Optional<SponsorDto> findBySubscriptionId(UUID subscriptionId);
    UUID createSponsor(Sponsor sponsor);
    void updateSponsor(UpdateSponsorDto sponsor);
}
