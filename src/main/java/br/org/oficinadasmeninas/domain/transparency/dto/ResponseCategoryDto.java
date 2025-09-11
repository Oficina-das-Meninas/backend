package br.org.oficinadasmeninas.domain.transparency.dto;

import java.util.UUID;

public record ResponseCategoryDto(
    UUID id,
    String name,
    Boolean isImage,
    Integer priority
){
}

