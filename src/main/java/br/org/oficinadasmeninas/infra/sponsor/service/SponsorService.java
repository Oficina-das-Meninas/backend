package br.org.oficinadasmeninas.infra.sponsor.service;

import br.org.oficinadasmeninas.domain.sponsor.Sponsor;
import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.dto.UpdateSponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.repository.ISponsorRepository;
import br.org.oficinadasmeninas.domain.sponsor.service.ISponsorService;

import java.util.Optional;
import java.util.UUID;

public class SponsorService implements ISponsorService {

    private final ISponsorRepository sponsorRepository;

    public SponsorService(ISponsorRepository sponsorRepository) {
        this.sponsorRepository = sponsorRepository;
    }

    @Override
    public Optional<SponsorDto> getSponsorbyId(UUID id) {
        return sponsorRepository.findById(id);
    }

    @Override
    public Optional<SponsorDto> getBySubscriptionId(UUID subscriptionId) {
        return sponsorRepository.findBySubscriptionId(subscriptionId);
    }

    @Override
    public UUID createSponsor(SponsorDto sponsorDto) {
        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorDto.id());
        sponsor.setMonthlyAmount(sponsorDto.monthlyAmount());
        sponsor.setBillingDay(sponsorDto.billingDay());
        sponsor.setUserId(sponsorDto.userId());
        sponsor.setSponsorSince(sponsorDto.sponsorSince());
        sponsor.setSponsorUntil(sponsorDto.sponsorUntil());
        sponsor.setIsActive(sponsorDto.isActive());
        sponsor.setSubscriptionId(sponsorDto.subscriptionId());

        return sponsorRepository.createSponsor(sponsor);
    }

    @Override
    public void updateSponsor(UpdateSponsorDto sponsor) {
        sponsorRepository.updateSponsor(sponsor);
    }
}
