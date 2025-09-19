package br.org.oficinadasmeninas.presentation.controller;


import br.org.oficinadasmeninas.domain.donation.dto.checkout.RequestCheckoutDTO;
import br.org.oficinadasmeninas.domain.donation.dto.checkout.ResponseCreateCheckoutDTO;
import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/donation")
@CrossOrigin(origins = "*")
public class DonationController {

    private IDonationService donationService;

    public DonationController(IDonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping("/criar")
    public ResponseCreateCheckoutDTO criarDonation(@RequestBody RequestCheckoutDTO request) {
        return donationService.createCheckout(request);
    }

}
