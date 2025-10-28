package br.org.oficinadasmeninas.domain.donation.mapper;

import br.org.oficinadasmeninas.domain.donation.Donation;
import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationDto;

import java.time.LocalDateTime;

public final class DonationMapper {

    public static Donation toEntity(CreateDonationDto request) {
        var donation = new Donation();
        donation.setValue(request.value());
        donation.setStatus(request.status());
        donation.setDonationAt(LocalDateTime.now());
        donation.setUserId(request.userId());

        return donation;
    }

    public static DonationDto toDto(Donation donation) {

        return new DonationDto(
                donation.getId(),
                donation.getValue(),
                donation.getDonationAt(),
                donation.getUserId(),
                donation.getStatus()
        );
    }
}
