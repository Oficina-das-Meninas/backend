package br.org.oficinadasmeninas.domain.transparency.dto;

public record RequestCategoryDto(
    String name,
    Boolean isImage,
    Integer priority
){
}
