package br.org.oficinadasmeninas.domain.donation.dto;

import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DonationWithDonorDto(
        UUID id,
        BigDecimal value,
        LocalDateTime donationAt,
        UUID userId,
        String donationType,
        DonationStatusEnum status,
        String sponsorStatus,
        String donorName
) {
}
