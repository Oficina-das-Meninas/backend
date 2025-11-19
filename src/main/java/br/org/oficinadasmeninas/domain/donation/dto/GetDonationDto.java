package br.org.oficinadasmeninas.domain.donation.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record GetDonationDto(
    int page,
    int pageSize,
    String donationType,
    String donorName,
    LocalDateTime startDate,
    LocalDateTime endDate,
    String sortField,
    String sortDirection
) {
    public static GetDonationDto FromRequestParams(
            int page,
            int pageSize,
            String donationType,
            String donorName,
            LocalDate startDate,
            LocalDate endDate,
            String sortField,
            String sortDirection
    ) {
        return new GetDonationDto(
                page,
                pageSize,
                donationType,
                donorName,
                startDate != null ? startDate.atStartOfDay() : null,
                endDate != null ? endDate.atTime(23, 59, 59) : null,
                sortField,
                sortDirection
        );
    }
}
