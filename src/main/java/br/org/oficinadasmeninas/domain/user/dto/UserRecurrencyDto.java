package br.org.oficinadasmeninas.domain.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRecurrencyDto(
		UUID id,
	    int billingDay,
	    LocalDateTime startDate,
	    Boolean isActive,
	    String subscriptionId,
	    UUID userId,
	    LocalDateTime cancelDate,
	    UUID donationId,
	    double value
) {	
}
