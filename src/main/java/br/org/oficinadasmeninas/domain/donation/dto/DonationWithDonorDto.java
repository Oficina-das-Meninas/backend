package br.org.oficinadasmeninas.domain.donation.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DonationWithDonorDto(
        UUID id,
        BigDecimal value,
        LocalDateTime donationAt,
        UUID userId,
        String donationType,
        String sponsorStatus,
        String donorName
) {
}
