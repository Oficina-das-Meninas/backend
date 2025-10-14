package br.org.oficinadasmeninas.domain.sponsor.service;

import java.util.List;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.dto.UpdateSponsorDto;

public interface ISponsorService {
	List<SponsorDto> getAllSponsors();

	SponsorDto getSponsorbyId(UUID id);

	SponsorDto getBySubscriptionId(UUID subscriptionId);

	SponsorDto getUserbyId(UUID id);

	UUID createSponsor(SponsorDto sponsor);

	void updateSponsor(UpdateSponsorDto sponsor);
	
	void suspendSponsor(UUID id);
}
