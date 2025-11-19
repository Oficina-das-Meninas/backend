package br.org.oficinadasmeninas.domain.donation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.paymentgateway.PaymentGatewayEnum;

public record DonationDto(
    UUID id,
    double value,
    String checkoutId,
    PaymentGatewayEnum gateway,
    UUID sponsorshipId,
    PaymentMethodEnum method,
    UUID userId,
    LocalDateTime donationAt
) {

}
