package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.application.DonationApplication;
import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationCheckoutDto;
import br.org.oficinadasmeninas.domain.donation.dto.GetDonationDto;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.infra.donation.service.DonationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/donations")
public class DonationController extends BaseController {

    private final DonationApplication donationApplication;
    private final DonationService donationService;

    public DonationController(DonationApplication donationApplication, DonationService donationService) {
        this.donationApplication = donationApplication;
        this.donationService = donationService;
    }

    @Operation(summary = "Lista doações filtradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doações encontradas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> findByFilter(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                          @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize,
                                          @RequestParam @Nullable String donationType,
                                          @RequestParam @Nullable String searchTerm,
                                          @RequestParam @Nullable LocalDate startDate,
                                          @RequestParam @Nullable LocalDate endDate,
                                          @RequestParam(defaultValue = "donationAt") String sortField,
                                          @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        return handle(() -> donationService.findByFilter(
                GetDonationDto.FromRequestParams(page, pageSize, donationType,
                        searchTerm, startDate,
                        endDate, sortField, sortDirection)
        ));
    }

    @Operation(summary = "Cria um checkout de doação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Checkout de doação criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados para criação do checkout")
    })
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

    @Operation(summary = "Cancelar uma assinatura de doação recorrente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Messages.RECURRING_DONATION_SUBSCRIPTION_CANCELED_SUCCESSFULLY),
            @ApiResponse(responseCode = "404", description = Messages.RECURRING_DONATION_SUBSCRIPTION_NOT_FOUND),
    })
    @DeleteMapping("/recurring")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> cancelRecurringDonationSubscription(
    ) {
        return handle(
                () -> {
                    donationApplication.cancelRecurringDonationSubscription();
                    return null;
                },
                Messages.RECURRING_DONATION_SUBSCRIPTION_CANCELED_SUCCESSFULLY,
                HttpStatus.OK
        );
    }

    @Operation(summary = "Buscar a assinatura de doação recorrente do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Messages.RECURRING_DONATION_SUBSCRIPTION_FOUND_SUCCESSFULLY),
            @ApiResponse(responseCode = "404", description = Messages.RECURRING_DONATION_SUBSCRIPTION_NOT_FOUND),
    })
    @GetMapping("/recurring")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getRecurringDonationSubscription(
    ) {
        return handle(
                donationApplication::getRecurringDonationSubscriptionByUserSession,
                Messages.RECURRING_DONATION_SUBSCRIPTION_FOUND_SUCCESSFULLY,
                HttpStatus.OK
        );
    }
}
