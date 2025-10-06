package br.org.oficinadasmeninas.domain.donation.dto;

import br.org.oficinadasmeninas.presentation.shared.notations.DonationNotation;

@DonationNotation.ValidDonationItem
public record CreateDonationCheckoutDto(DonorInfoDto donor, DonationItemDto donation) {
}
