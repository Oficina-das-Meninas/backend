package br.org.oficinadasmeninas.domain.donation.dto;

import java.util.Optional;

public record DonationItemDto(Long value, boolean isRecurring, Optional<Integer> cycles) {

}
