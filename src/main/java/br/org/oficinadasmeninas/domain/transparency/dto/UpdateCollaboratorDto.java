package br.org.oficinadasmeninas.domain.transparency.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCollaboratorDto (
        @NotNull(message = "Priority deve ser um número maior ou igual a 0")
        @Min(value = 0, message = "Priority deve ser um número maior ou igual a 0")
        Integer priority
){}
