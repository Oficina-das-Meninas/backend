package br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout;

import java.util.UUID;

public record RequestCreateCheckoutDto(UUID internalId, RequestCreateCheckoutCustomerDto customerDto, RequestCreateCheckoutSignatureDto signatureDto, RequestCreateCheckoutDonationDto donation) {}
