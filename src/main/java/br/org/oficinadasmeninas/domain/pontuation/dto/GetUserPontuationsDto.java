package br.org.oficinadasmeninas.domain.pontuation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GetUserPontuationsDto(
        int page,
        int pageSize,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String donationMethod
) {
    public static GetUserPontuationsDto FromRequestParams(
            int page,
            int pageSize,
            LocalDate startDate,
            LocalDate endDate,
            String donationMethod
    ) {
        return new GetUserPontuationsDto(
                page,
                pageSize,
                startDate != null ? startDate.atStartOfDay() : null,
                endDate != null ? endDate.atTime(23, 59, 59) : null,
                donationMethod
        );
    }
}