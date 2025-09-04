package br.org.oficinadasmeninas.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    private UUID id;
    private String title;
    private String previewImageUrl;
    private String description;
    private BigDecimal amount;
    private LocalDateTime eventDate;
    private String location;
    private String urlToPlatform;

    public Event(UUID id, String title, String previewImageUrl, String description, BigDecimal amount, LocalDateTime eventDate, String location, String urlToPlatform) {
        this.id = id;
        this.title = title;
        this.previewImageUrl = previewImageUrl;
        this.description = description;
        this.amount = amount;
        this.eventDate = eventDate;
        this.location = location;
        this.urlToPlatform = urlToPlatform;
    }

    public UUID getId() { return id; }

    public String getTitle() { return title; }

    public String getPreviewImageUrl() { return previewImageUrl; }

    public String getDescription() { return description; }

    public BigDecimal getAmount() { return amount; }

    public LocalDateTime getEventDate() { return eventDate; }

    public String getLocation() { return location; }

    public String getUrlToPlatform() { return urlToPlatform; }
}