package br.org.oficinadasmeninas.domain.donation.dto;

import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GetDonationDto(
    int page,
    int pageSize,
    String donationType,
    DonationStatusEnum status,
    String donorName,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
    public static GetDonationDto FromRequestParams(
            int page,
            int pageSize,
            String donationType,
            DonationStatusEnum status,
            String donorName,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return new GetDonationDto(
                page,
                pageSize,
                donationType,
                status,
                donorName,
                startDate != null ? startDate.atStartOfDay() : null,
                endDate != null ? endDate.atTime(23, 59, 59) : null
        );
    }
}
