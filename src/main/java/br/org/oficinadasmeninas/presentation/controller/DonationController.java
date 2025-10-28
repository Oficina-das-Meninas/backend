package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.application.DonationApplication;
import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationCheckoutDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationCheckoutDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationWithDonorDto;
import br.org.oficinadasmeninas.domain.donation.dto.GetDonationDto;
import br.org.oficinadasmeninas.infra.donation.service.DonationService;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

	private final DonationApplication donationApplication;
    private final DonationService donationService;

    public DonationController(DonationApplication donationApplication, DonationService donationService) {
		this.donationApplication = donationApplication;
        this.donationService = donationService;
    }

	@PostMapping("/create")
	public DonationCheckoutDto createDonationCheckout(@Valid @RequestBody CreateDonationCheckoutDto donationCheckout) {
		return donationApplication.createDonationCheckout(donationCheckout);
	}

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public PageDTO<DonationWithDonorDto> getFilteredDonation(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                             @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize,
                                                             @RequestParam @Nullable String donationType,
                                                             @RequestParam @Nullable String status,
                                                             @RequestParam @Nullable String searchTerm,
                                                             @RequestParam @Nullable LocalDate startDate,
                                                             @RequestParam @Nullable LocalDate endDate
    ) {
        DonationStatusEnum donationStatus = status != null ? DonationStatusEnum.valueOf(status) : null;

        return donationService.getFilteredDonations(
                GetDonationDto.FromRequestParams(page, pageSize, donationType, donationStatus, searchTerm, startDate, endDate)
        );
    }
}
