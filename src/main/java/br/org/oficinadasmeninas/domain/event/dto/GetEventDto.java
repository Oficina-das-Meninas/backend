package br.org.oficinadasmeninas.domain.event.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GetEventDto (
    int page,
    int pageSize,
    String title,
    String description,
    String location,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
    public static GetEventDto FromRequestParams(
            int page,
            int pageSize,
            String title,
            String description,
            String location,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return new GetEventDto(
                page,
                pageSize,
                title,
                description,
                location,
                startDate != null ? startDate.atStartOfDay() : null,
                endDate != null ? endDate.atTime(23, 59, 59) : null
        );
    }
}