package br.org.oficinadasmeninas.domain.event;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    private UUID id;
    private String title;
    private String previewImageUrl;
    private String partnersImageUrl;
    private String description;
    private BigDecimal amount;
    private LocalDateTime eventDate;
    private String location;
    private String urlToPlatform;

    public Event(UUID id,
                 String title,
                 String previewImageUrl,
                 String partnersImageUrl,
                 String description,
                 BigDecimal amount,
                 LocalDateTime eventDate,
                 String location,
                 String urlToPlatform
    ) {
        this.id = id;
        this.title = title;
        this.previewImageUrl = previewImageUrl;
        this.partnersImageUrl = partnersImageUrl;
        this.description = description;
        this.amount = amount;
        this.eventDate = eventDate;
        this.location = location;
        this.urlToPlatform = urlToPlatform;
    }

    public UUID getId() { return id; }

    public void SetId(UUID id) { this.id = id; }

    public String getTitle() { return title; }

    public void SetTitle(String title) { this.title = title; }

    public String getPreviewImageUrl() { return previewImageUrl; }

    public void SetPreviewImageUrl(String previewImageUrl) { this.previewImageUrl = previewImageUrl; }

    public String getPartnersImageUrl() { return partnersImageUrl; }

    public void setPartnersImageUrl(String partnersImageUrl) { this.partnersImageUrl = partnersImageUrl; }

    public String getDescription() { return description; }

    public void SetDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }

    public void SetAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getEventDate() { return eventDate; }

    public void SetEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public String getLocation() { return location; }

    public void SetLocation(String location) { this.location = location; }

    public String getUrlToPlatform() { return urlToPlatform; }

    public void SetUrlToPlatform(String urlToPlatform) { this.urlToPlatform = urlToPlatform; }

    public void setPreviewImageUrl(String previewFileName) {
        previewImageUrl = previewFileName;
    }
}