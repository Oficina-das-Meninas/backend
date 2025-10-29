package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.application.DonationApplication;
import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationCheckoutDto;
import br.org.oficinadasmeninas.domain.resources.Messages;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/donations")
public class DonationController extends BaseController {

    private final DonationApplication donationApplication;

    public DonationController(DonationApplication donationApplication) {
        this.donationApplication = donationApplication;
    }

    @PostMapping
    public ResponseEntity<?> createDonationCheckout(
            @Valid @RequestBody CreateDonationCheckoutDto donationCheckout
    ) {
        return handle(
                () -> donationApplication.createDonationCheckout(donationCheckout),
                Messages.DONATION_CREATED_SUCCESSFULLY,
                HttpStatus.CREATED
        );
    }
}
