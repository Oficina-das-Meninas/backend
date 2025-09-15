package br.org.oficinadasmeninas.domain.event.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateEventDto {

    @NotNull(message = "O arquivo da imagem é obrigatório.")
    private MultipartFile eventImage;

    private UUID id;

    @NotBlank(message = "O titulo do evento é obrigatório")
    @Size(max = 255, message = "O título do evento deve ter no máximo 255 caracteres.")
    private String title;

    @NotBlank(message = "A URL de pré-visualização de imagem do evento é obrigatória")
    private String previewImageUrl;

    @NotBlank(message = "A descrição do evento é obrigatória")
    private String description;

    private BigDecimal amount;

    @NotNull(message = "A data do evento é obrigatória.")
    @Future(message = "A data do evento deve ser futura.")
    private LocalDateTime eventDate;

    @NotBlank(message = "A localização do evento é obrigatória")
    private String location;

    @NotBlank(message = "A url para a plataforma do evento é obrigatória")
    private String urlToPlatform;

    public UpdateEventDto() { }

    public MultipartFile getEventImage() {
        return eventImage;
    }

    public void setEventImage(MultipartFile eventImage) {
        this.eventImage = eventImage;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrlToPlatform() {
        return urlToPlatform;
    }

    public void setUrlToPlatform(String urlToPlatform) {
        this.urlToPlatform = urlToPlatform;
    }
}
