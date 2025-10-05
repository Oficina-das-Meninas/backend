package br.org.oficinadasmeninas.domain.sponsor.service;

import br.org.oficinadasmeninas.domain.sponsor.Sponsor;
import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.dto.UpdateSponsorDto;

import java.util.Optional;
import java.util.UUID;

public interface ISponsorService {
    Optional<SponsorDto> getSponsorbyId(UUID id);
    Optional<SponsorDto> getBySubscriptionId(UUID subscriptionId);
    UUID createSponsor(SponsorDto sponsor);
    void updateSponsor(UpdateSponsorDto sponsor);
}
