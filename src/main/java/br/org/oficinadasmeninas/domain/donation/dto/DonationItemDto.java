package br.org.oficinadasmeninas.domain.donation.dto;


import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public record DonationItemDto(
        @NotNull(message = "O valor da doação é obrigatório")
        double value,
        @NotNull(message = "O campo isRecurring é obrigatório")
        boolean isRecurring,
        Optional<Integer> cycles
) {}
