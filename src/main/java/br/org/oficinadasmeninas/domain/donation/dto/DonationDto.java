package br.org.oficinadasmeninas.domain.donation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;

public record DonationDto(UUID id, double value, LocalDateTime donationAt, UUID userId, DonationStatusEnum status) {

}
