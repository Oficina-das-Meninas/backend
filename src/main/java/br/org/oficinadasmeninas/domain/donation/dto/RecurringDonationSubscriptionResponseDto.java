package br.org.oficinadasmeninas.domain.donation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RecurringDonationSubscriptionResponseDto(
    UUID subscriptionId,
    int billingDay,
    Boolean isActive,
    LocalDateTime startDate,
    LocalDateTime cancelDate
) {}

