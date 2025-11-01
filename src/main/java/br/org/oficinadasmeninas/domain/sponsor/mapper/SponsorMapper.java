package br.org.oficinadasmeninas.domain.sponsor.mapper;

import br.org.oficinadasmeninas.domain.sponsor.Sponsor;
import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;

public final class SponsorMapper {

    public static SponsorDto toDto(Sponsor sponsor) {
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
