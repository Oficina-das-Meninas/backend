package br.org.oficinadasmeninas.domain.donation.dto;

import java.util.UUID;

import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;

public record CreateDonationDto(double value, UUID userId, DonationStatusEnum status) {

}
