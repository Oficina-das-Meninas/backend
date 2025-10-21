package br.org.oficinadasmeninas.domain.donation.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DonorInfoDto(
        @NotBlank(message = "O nome é obrigatório")
        String name,
        @Email(message = "O email deve ser válido")
        @NotBlank(message = "O email é obrigatório")
        String email,
        @NotBlank(message = "O documento é obrigatório")
        String document,
        @NotBlank(message = "O telefone é obrigatório")
        String phone,
        UUID id
) {}
