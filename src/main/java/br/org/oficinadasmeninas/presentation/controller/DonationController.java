package br.org.oficinadasmeninas.presentation.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.oficinadasmeninas.application.DonationApplication;
import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationCheckoutDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationCheckoutDto;

@RestController
@RequestMapping("/api/donation")
public class DonationController {

	private final DonationApplication donationApplication;

	public DonationController(DonationApplication donationApplication) {
		this.donationApplication = donationApplication;
	}

	@PostMapping("/create")
	public DonationCheckoutDto createDonationCheckout(@Valid @RequestBody CreateDonationCheckoutDto donationCheckout) {
		return donationApplication.createDonationCheckout(donationCheckout);
	}
	
}
