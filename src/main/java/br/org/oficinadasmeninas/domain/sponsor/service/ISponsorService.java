package br.org.oficinadasmeninas.domain.sponsor.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.dto.UpdateSponsorDto;

@Repository
public interface ISponsorService {
    Optional<SponsorDto> getSponsorbyId(UUID id);
    List<SponsorDto> getSponsorByUserId(UUID id);
    Optional<SponsorDto> getActiveSponsorByUserId(UUID id);
    Optional<SponsorDto> getBySubscriptionId(UUID subscriptionId);
    UUID createSponsor(SponsorDto sponsor);
    void updateSponsor(UpdateSponsorDto sponsor);
    void activeSponsorByUserId(UUID userId);
}
