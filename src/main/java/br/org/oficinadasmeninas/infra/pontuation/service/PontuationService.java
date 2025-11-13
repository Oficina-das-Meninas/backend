package br.org.oficinadasmeninas.infra.pontuation.service;

import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.pontuation.Bonuses;
import br.org.oficinadasmeninas.domain.pontuation.Pontuation;
import br.org.oficinadasmeninas.domain.pontuation.dto.GetUserPontuationsDto;
import br.org.oficinadasmeninas.domain.pontuation.dto.PontuationDto;
import br.org.oficinadasmeninas.domain.pontuation.repository.IPontuationRepository;
import br.org.oficinadasmeninas.domain.pontuation.service.IPontuationService;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PontuationService implements IPontuationService {

    private final IPontuationRepository pontuationRepository;

    public PontuationService(IPontuationRepository pontuationRepository) {
        this.pontuationRepository = pontuationRepository;
    }

    @Override
    public PageDTO<PontuationDto> getUserPontuations(UUID id, GetUserPontuationsDto request) {
        var pontuations = pontuationRepository.findByIdAndFilters(id, request);

        var pontuationDTOs = pontuations.contents().stream()
                .map(p -> new PontuationDto(
                        p.getId(),
                        p.getDonatedValue(),
                        p.getDonatedDate(),
                        p.getEarnedPoints(),
                        p.getTotalPoints(),
                        PaymentMethodEnum.fromString(p.getMethod()),
                        buildBonuses(p),
                        p.getRecurrenceSequence()
                ))
                .toList();

        return new PageDTO<>(pontuationDTOs, pontuations.totalElements(), pontuations.totalPages());
    }

    private Bonuses buildBonuses(Pontuation pontuation) {
        Bonuses bonuses = new Bonuses();

        bonuses.setFirstDonationBonus(pontuation.isFirstDonation() ? 100L : 0L);
        bonuses.setRecurrenceBonus(
                pontuation.getRecurrenceSequence() > 1
                        ? (long) Math.floor(pontuation.getDonatedValue() * (pontuation.getRecurrenceSequence() / 100.0))
                        : 0L
        );
        bonuses.setValueBonus(pontuation.getDonatedValue());

        return bonuses;
    }
}