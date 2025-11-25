package br.org.oficinadasmeninas.infra.account.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordDto(@NotBlank(message = "A nova senha é obrigatória") String newPassword) { }
