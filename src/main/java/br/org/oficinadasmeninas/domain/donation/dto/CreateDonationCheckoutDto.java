package br.org.oficinadasmeninas.domain.donation.dto;

import br.org.oficinadasmeninas.presentation.shared.notations.ValidDonationCheckout;
import jakarta.validation.Valid;

@ValidDonationCheckout
public record CreateDonationCheckoutDto(@Valid DonorInfoDto donor, @Valid DonationItemDto donation) {
}
