package br.org.oficinadasmeninas.domain.event.dto;

import java.time.LocalDateTime;

public record GetEventDto (
    int page,
    int pageSize,
    String title,
    String description,
    String location,
    LocalDateTime startDate,
    LocalDateTime endDate
) { }