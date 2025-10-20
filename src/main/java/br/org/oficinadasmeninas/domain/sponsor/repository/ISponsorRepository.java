package br.org.oficinadasmeninas.domain.sponsor.repository;

import br.org.oficinadasmeninas.domain.sponsor.Sponsor;
import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.dto.UpdateSponsorDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ISponsorRepository {
	List<Sponsor> findAllSponsors();

	Optional<Sponsor> findById(UUID id);

	Optional<Sponsor> findBySubscriptionId(UUID subscriptionId);

	List<Sponsor> findByUserId(UUID id);

    Optional<Sponsor> findActiveByUserId(UUID id);

	UUID createSponsor(Sponsor sponsor);

	void updateSponsor(UpdateSponsorDto sponsor);
	
	void suspendSponsor(UUID id);

    void activeSponsorByuserId(UUID userId);

}
