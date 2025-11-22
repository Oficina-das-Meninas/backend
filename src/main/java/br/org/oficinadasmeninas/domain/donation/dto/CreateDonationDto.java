package br.org.oficinadasmeninas.domain.donation.dto;

import java.util.UUID;

import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.paymentgateway.PaymentGatewayEnum;

public record CreateDonationDto(
    double value,
    String checkoutId,
    PaymentGatewayEnum gateway,
    UUID sponsorshipId,
    PaymentMethodEnum method,
    UUID userId,
    UUID referenceId
) {

}
