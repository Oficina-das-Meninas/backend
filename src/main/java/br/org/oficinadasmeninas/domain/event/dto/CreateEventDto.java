package br.org.oficinadasmeninas.domain.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateEventDto(

        @NotNull(message = "O arquivo da imagem de pré visualização é obrigatório.")
        MultipartFile previewImage,

        MultipartFile partnersImage,

        @NotBlank(message = "O titulo do evento é obrigatório")
        @Size(max = 255, message = "O título do evento deve ter no máximo 255 caracteres.")
        String title,

        @NotBlank(message = "A descrição do evento é obrigatória")
        String description,

        BigDecimal amount,

        @NotNull(message = "A data do evento é obrigatória.")
        LocalDateTime eventDate,

        @NotBlank(message = "A localização do evento é obrigatória")
        String location,

        @NotBlank(message = "A url para a plataforma do evento é obrigatória")
        String urlToPlatform
) {}
