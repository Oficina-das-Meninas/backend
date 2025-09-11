package br.org.oficinadasmeninas.domain.transparency.dto;

public record CreateCategoryDto(
    String name,
    Boolean isImage,
    Integer priority
){
}
