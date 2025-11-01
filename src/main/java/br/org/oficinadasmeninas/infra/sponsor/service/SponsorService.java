package br.org.oficinadasmeninas.infra.sponsor.service;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.sponsor.Sponsor;
import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.dto.UpdateSponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.mapper.SponsorMapper;
import br.org.oficinadasmeninas.domain.sponsor.repository.ISponsorRepository;
import br.org.oficinadasmeninas.domain.sponsor.service.ISponsorService;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
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
    public Optional<SponsorDto> findById(UUID id) {
        Optional<Sponsor> sponsor = sponsorRepository.findById(id);
        return sponsor.map(SponsorMapper::toDto);
    }

    @Override
    public List<SponsorDto> findByUserId(UUID id) {
        return sponsorRepository.findByUserId(id)
                .stream()
                .map(SponsorMapper::toDto)
                .toList();
    }

    @Override
    public Optional<SponsorDto> findActiveByUserId(UUID id) {
        return sponsorRepository.findActiveByUserId(id)
                .map(SponsorMapper::toDto);
    }

    @Override
    public Optional<SponsorDto> findBySubscriptionId(UUID subscriptionId) {
        return sponsorRepository.findBySubscriptionId(subscriptionId)
                .map(SponsorMapper::toDto);
    }

    @Override
    public UUID insert(SponsorDto sponsorDto) {
        var sponsor = new Sponsor();
        sponsor.setMonthlyAmount(sponsorDto.monthlyAmount());
        sponsor.setBillingDay(sponsorDto.billingDay());
        sponsor.setUserId(sponsorDto.userId());
        sponsor.setSponsorSince(sponsorDto.sponsorSince());
        sponsor.setSponsorUntil(sponsorDto.sponsorUntil());
        sponsor.setIsActive(sponsorDto.isActive());
        sponsor.setSubscriptionId(sponsorDto.subscriptionId());

        sponsorRepository.insert(sponsor);
        return sponsor.getId();
    }

    @Override
    public void activateByUserId(UUID userId) {
        sponsorRepository.activateByUserId(userId);
    }

    @Override
    public void update(UpdateSponsorDto request) {

        var sponsor = sponsorRepository.findById(request.id())
                .orElseThrow(() -> new NotFoundException(Messages.SPONSOR_NOT_FOUND + request.id()));

        sponsor.setSponsorUntil(request.sponsorUntil());
        sponsor.setIsActive(request.isActive());
        sponsor.setSubscriptionId(request.subscriptionId());

        sponsorRepository.update(sponsor);
    }
}
