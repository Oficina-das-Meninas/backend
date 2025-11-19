package br.org.oficinadasmeninas.domain.donation.mapper;

import br.org.oficinadasmeninas.domain.donation.Donation;
import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationDto;

import java.time.LocalDateTime;

public final class DonationMapper {

    public static Donation toEntity(CreateDonationDto request) {
        var donation = new Donation();
        donation.setValue(request.value());
        donation.setCheckoutId(request.checkoutId());
        donation.setGateway(request.gateway());
        donation.setSponsorshipId(request.sponsorshipId());
        donation.setMethod(request.method());
        donation.setUserId(request.userId());
        donation.setDonationAt(LocalDateTime.now());

        return donation;
    }

    public static DonationDto toDto(Donation donation) {

        return new DonationDto(
                donation.getId(),
                donation.getValue(),
                donation.getCheckoutId(),
                donation.getGateway(),
                donation.getSponsorshipId(),
                donation.getMethod(),
                donation.getUserId(),
                donation.getDonationAt()
        );
    }
}
