package br.org.oficinadasmeninas.domain.partner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UpdatePartnerDto(
        @NotNull(message = "O arquivo da imagem de pré visualização é obrigatório.")
        MultipartFile previewImage,

        @NotNull(message = "O id do patrocinador é obrigatório.")
        UUID id,

        @NotBlank(message = "O nome do patrocinador é obrigatório")
        @Size(max = 500, message = "O nome do patrocinador deve ter no máximo 500 caracteres.")
        String name
) {
 
}