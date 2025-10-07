package br.org.oficinadasmeninas.domain.donation.dto;

import br.org.oficinadasmeninas.presentation.shared.notations.DonationNotation;
import jakarta.validation.Valid;

public record CreateDonationCheckoutDto(@Valid DonorInfoDto donor, @Valid DonationItemDto donation) {
}
