package br.org.oficinadasmeninas.domain.donation.dto;

import br.org.oficinadasmeninas.domain.paymentgateway.PaymentGatewayEnum;
import br.org.oficinadasmeninas.presentation.shared.notations.DonationNotation;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

@DonationNotation.ValidDonationItem
public record DonationItemDto(
        @NotNull(message = "O valor da doação é obrigatório")
        double value,
        @NotNull(message = "O campo isRecurring é obrigatório")
        boolean isRecurring,
        Optional<Integer> cycles,
        @NotNull(message = "O gateway de pagamento é obrigatório")
        PaymentGatewayEnum gatewayPayment
) {}
