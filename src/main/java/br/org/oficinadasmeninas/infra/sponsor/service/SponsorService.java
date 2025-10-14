package br.org.oficinadasmeninas.infra.sponsor.service;

import java.util.List;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.shared.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.domain.sponsor.Sponsor;
import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.dto.UpdateSponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.repository.ISponsorRepository;
import br.org.oficinadasmeninas.domain.sponsor.service.ISponsorService;

public class SponsorService implements ISponsorService {

    private final ISponsorRepository sponsorRepository;

    public SponsorService(ISponsorRepository sponsorRepository) {
        this.sponsorRepository = sponsorRepository;
    }
    
    @Override
	public List<SponsorDto> getAllSponsors() {
		return sponsorRepository.findAllSponsors().stream().map(sponsor -> 
			new SponsorDto(
				sponsor.getId(), 
				sponsor.getMonthlyAmount(), 
				sponsor.getBillingDay(), 
				sponsor.getUserId(),
				sponsor.getSponsorSince(),
				sponsor.getSponsorUntil(),
				sponsor.getIsActive(),
				sponsor.getSubscriptionId()
				)
			).toList();
	}

    @Override
    public SponsorDto getSponsorbyId(UUID id) {
		Sponsor sponsor = sponsorRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Padrinho não encontrado com id: " + id));
		
		SponsorDto sponsorDto = new SponsorDto(
				sponsor.getId(), 
				sponsor.getMonthlyAmount(), 
				sponsor.getBillingDay(), 
				sponsor.getUserId(),
				sponsor.getSponsorSince(),
				sponsor.getSponsorUntil(),
				sponsor.getIsActive(),
				sponsor.getSubscriptionId()
				);

		return sponsorDto;
    }

    @Override
    public SponsorDto getBySubscriptionId(UUID subscriptionId) {
    	Sponsor sponsor = sponsorRepository.findBySubscriptionId(subscriptionId)
				.orElseThrow(() -> new EntityNotFoundException("Padrinho não encontrado com subscriptionId: " + subscriptionId));
		
		SponsorDto sponsorDto = new SponsorDto(
				sponsor.getId(), 
				sponsor.getMonthlyAmount(), 
				sponsor.getBillingDay(), 
				sponsor.getUserId(),
				sponsor.getSponsorSince(),
				sponsor.getSponsorUntil(),
				sponsor.getIsActive(),
				sponsor.getSubscriptionId()
				);

		return sponsorDto;
    }
    
    @Override
	public SponsorDto getUserbyId(UUID id) {
    	Sponsor sponsor = sponsorRepository.findByUserId(id)
				.orElseThrow(() -> new EntityNotFoundException("Padrinho não encontrado com usuário: " + id));
		
		SponsorDto sponsorDto = new SponsorDto(
				sponsor.getId(), 
				sponsor.getMonthlyAmount(), 
				sponsor.getBillingDay(), 
				sponsor.getUserId(),
				sponsor.getSponsorSince(),
				sponsor.getSponsorUntil(),
				sponsor.getIsActive(),
				sponsor.getSubscriptionId()
				);

		return sponsorDto;
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

	@Override
	public void suspendSponsor(UUID id) {
		sponsorRepository.suspendSponsor(id);
	}
	
}
