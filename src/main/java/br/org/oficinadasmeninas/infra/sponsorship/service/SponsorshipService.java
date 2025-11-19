package br.org.oficinadasmeninas.infra.sponsorship.service;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.sponsorship.Sponsorship;
import br.org.oficinadasmeninas.domain.sponsorship.dto.SponsorshipDto;
import br.org.oficinadasmeninas.domain.sponsorship.dto.UpdateSponsorshipDto;
import br.org.oficinadasmeninas.domain.sponsorship.mapper.SponsorshipMapper;
import br.org.oficinadasmeninas.domain.sponsorship.repository.ISponsorshipRepository;
import br.org.oficinadasmeninas.domain.sponsorship.service.ISponsorshipService;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SponsorshipService implements ISponsorshipService {

    private final ISponsorshipRepository sponsorshipRepository;

    public SponsorshipService(ISponsorshipRepository sponsorshipRepository) {
        this.sponsorshipRepository = sponsorshipRepository;
    }

    @Override
    public Optional<SponsorshipDto> findById(UUID id) {
        Optional<Sponsorship> sponsorship = sponsorshipRepository.findById(id);
        return sponsorship.map(SponsorshipMapper::toDto);
    }

    @Override
    public List<SponsorshipDto> findByUserId(UUID id) {
        return sponsorshipRepository.findByUserId(id)
                .stream()
                .map(SponsorshipMapper::toDto)
                .toList();
    }

    @Override
    public Optional<SponsorshipDto> findActiveByUserId(UUID id) {
        return sponsorshipRepository.findActiveByUserId(id)
                .map(SponsorshipMapper::toDto);
    }

    @Override
    public Optional<SponsorshipDto> findBySubscriptionId(String subscriptionId) {
        return sponsorshipRepository.findBySubscriptionId(subscriptionId)
                .map(SponsorshipMapper::toDto);
    }

    @Override
    public UUID insert(SponsorshipDto sponsorshipDto) {
        var sponsorship = new Sponsorship();
        sponsorship.setBillingDay(sponsorshipDto.billingDay());
        sponsorship.setStartDate(sponsorshipDto.startDate());
        sponsorship.setIsActive(sponsorshipDto.isActive());
        sponsorship.setSubscriptionId(sponsorshipDto.subscriptionId());
        sponsorship.setUserId(sponsorshipDto.userId());
        sponsorship.setCancelDate(sponsorshipDto.cancelDate());

        sponsorshipRepository.insert(sponsorship);
        return sponsorship.getId();
    }

    @Override
    public void activateByUserId(UUID userId) {
        sponsorshipRepository.activateByUserId(userId);
    }

    @Override
    public void update(UpdateSponsorshipDto request) {

        var sponsorship = sponsorshipRepository.findById(request.id())
                .orElseThrow(() -> new NotFoundException(Messages.SPONSOR_NOT_FOUND + request.id()));

        sponsorship.setCancelDate(request.cancelDate());
        sponsorship.setIsActive(request.isActive());
        sponsorship.setSubscriptionId(request.subscriptionId());

        sponsorshipRepository.update(sponsorship);
    }
}
