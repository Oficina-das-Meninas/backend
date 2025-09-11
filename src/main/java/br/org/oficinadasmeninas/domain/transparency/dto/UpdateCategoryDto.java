package br.org.oficinadasmeninas.domain.transparency.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCategoryDto(
    @NotBlank(message = "O nome é obrigatório")
    String name,

    @NotNull(message = "Priority deve ser um número maior ou igual a 0")
    @Min(value = 0, message = "Priority deve ser um número maior ou igual a 0")
    Integer priority
) {}
