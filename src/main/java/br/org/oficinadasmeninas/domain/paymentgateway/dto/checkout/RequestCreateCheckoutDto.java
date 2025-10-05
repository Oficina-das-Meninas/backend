package br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout;

public record RequestCreateCheckoutDto(String internalId, RequestCreateCheckoutCustomerDto customerDto, RequestCreateCheckoutSignatureDto signatureDto, RequestCreateCheckoutDonationDto donation) {}
