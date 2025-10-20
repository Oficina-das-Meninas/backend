package br.org.oficinadasmeninas.infra.sponsor.service;

import br.org.oficinadasmeninas.domain.sponsor.Sponsor;
import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.dto.UpdateSponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.repository.ISponsorRepository;
import br.org.oficinadasmeninas.domain.sponsor.service.ISponsorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SponsorService implements ISponsorService {

    private final ISponsorRepository sponsorRepository;

    public SponsorService(ISponsorRepository sponsorRepository) {
        this.sponsorRepository = sponsorRepository;
    }

    @Override
    public Optional<SponsorDto> getSponsorbyId(UUID id) {
        Optional<Sponsor> sponsor = sponsorRepository.findById(id);
        return sponsor.map(this::mapSponsor);
    }

    @Override
    public List<SponsorDto> getSponsorByUserId(UUID id) {
        return sponsorRepository.findByUserId(id)
                .stream()
                .map(this::mapSponsor)
                .toList();
    }

    @Override
    public Optional<SponsorDto> getActiveSponsorByUserId(UUID id) {
        return sponsorRepository.findActiveByUserId(id)
                .map(this::mapSponsor);
    }

    @Override
    public Optional<SponsorDto> getBySubscriptionId(UUID subscriptionId) {
        return sponsorRepository.findBySubscriptionId(subscriptionId)
                .map(this::mapSponsor);
    }

    @Override
    public UUID createSponsor(SponsorDto sponsorDto) {
        Sponsor sponsor = new Sponsor();
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
    public void activeSponsorByUserId(UUID userId) {
     sponsorRepository.activeSponsorByuserId(userId);
    }

    @Override
    public void updateSponsor(UpdateSponsorDto sponsor) {
        sponsorRepository.updateSponsor(sponsor);
    }

    private SponsorDto mapSponsor(Sponsor sponsor) {
        return new SponsorDto(
                sponsor.getMonthlyAmount(),
                sponsor.getBillingDay(),
                sponsor.getUserId(),
                sponsor.getSponsorSince(),
                sponsor.getSponsorUntil(),
                sponsor.getIsActive(),
                sponsor.getSubscriptionId()
        );
    }
}
