package br.org.oficinadasmeninas.domain.event.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GetEventDto (
    int page,
    int pageSize,
    String searchTerm,
    String description,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
    public static GetEventDto FromRequestParams(
            int page,
            int pageSize,
            String searchTerm,
            String description,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return new GetEventDto(
                page,
                pageSize,
                searchTerm,
                description,
                startDate != null ? startDate.atStartOfDay() : null,
                endDate != null ? endDate.atTime(23, 59, 59) : null
        );
    }
}