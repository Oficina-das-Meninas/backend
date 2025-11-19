package br.org.oficinadasmeninas.domain.sponsorship.mapper;

import br.org.oficinadasmeninas.domain.sponsorship.Sponsorship;
import br.org.oficinadasmeninas.domain.sponsorship.dto.SponsorshipDto;

public final class SponsorshipMapper {

    public static SponsorshipDto toDto(Sponsorship sponsorship) {
        return new SponsorshipDto(
                sponsorship.getId(),
                sponsorship.getBillingDay(),
                sponsorship.getStartDate(),
                sponsorship.getIsActive(),
                sponsorship.getSubscriptionId(),
                sponsorship.getUserId(),
                sponsorship.getCancelDate()
        );
    }
}
