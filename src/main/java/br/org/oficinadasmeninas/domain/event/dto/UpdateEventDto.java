package br.org.oficinadasmeninas.domain.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateEventDto(
        @NotNull(message = "O arquivo da imagem de pré visualização é obrigatório.")
        MultipartFile previewImage,

        MultipartFile partnersImage,

        Boolean isActive,

        @NotNull(message = "O id do evento é obrigatório.")
        UUID id,

        @NotBlank(message = "O titulo do evento é obrigatório")
        @Size(max = 4096, message = "O título do evento deve ter no máximo 4096 caracteres.")
        String title,

        @NotBlank(message = "A descrição do evento é obrigatória")
        String description,

        @NotNull(message = "A data do evento é obrigatória.")
        LocalDateTime eventDate,

        @NotBlank(message = "A localização do evento é obrigatória")
        String location,

        @NotBlank(message = "A url para a plataforma do evento é obrigatória")
        String urlToPlatform
) {
        public static UpdateEventDto forDeletion(UUID id) {
                return new UpdateEventDto(
                        null,
                        null,
                        false,
                        id,
                        null,
                        null,
                        null,
                        null,
                        null
                );
        }
}