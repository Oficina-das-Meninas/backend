package br.org.oficinadasmeninas.infra.pontuation.service;

import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.pontuation.Bonuses;
import br.org.oficinadasmeninas.domain.pontuation.Pontuation;
import br.org.oficinadasmeninas.domain.pontuation.dto.GetUserPontuationsDto;
import br.org.oficinadasmeninas.domain.pontuation.dto.PontuationDto;
import br.org.oficinadasmeninas.domain.pontuation.repository.IPontuationRepository;
import br.org.oficinadasmeninas.domain.pontuation.service.IPontuationService;
import br.org.oficinadasmeninas.domain.user.service.IUserService;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.stereotype.Service;

import static java.lang.Math.floor;

@Service
public class PontuationService implements IPontuationService {

    private final IPontuationRepository pontuationRepository;
    private final IUserService userService;

    public PontuationService(IPontuationRepository pontuationRepository, IUserService userService) {
        this.pontuationRepository = pontuationRepository;
        this.userService = userService;
    }

    @Override
    public PageDTO<PontuationDto> getUserPontuations(GetUserPontuationsDto request) {
        var user = userService.findByUserSession();

        var pontuations = pontuationRepository.findByIdAndFilters(user.getId(), request);

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

        long donatedValue = (long) floor(pontuation.getDonatedValue());

        bonuses.setFirstDonationBonus(pontuation.isFirstDonation() ? 100L : 0L);
        bonuses.setRecurrenceBonus(pontuation.isFirstDonation() ? 0L : pontuation.getEarnedPoints() - donatedValue);
        bonuses.setValueBonus(donatedValue);

        return bonuses;
    }
}