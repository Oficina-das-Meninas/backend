package br.org.oficinadasmeninas.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyPasswordDto (
        @NotBlank(message = "A senha é obrigatória")
        String password)
{}
