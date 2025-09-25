package br.org.oficinadasmeninas.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    private UUID id;
    private Boolean isActive;
    private String title;
    private String previewImageUrl;
    private String partnersImageUrl;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private String urlToPlatform;

    public Event(UUID id,
                 String title,
                 String previewImageUrl,
                 String partnersImageUrl,
                 String description,
                 LocalDateTime eventDate,
                 String location,
                 String urlToPlatform
    ) {
        this.id = id;
        this.title = title;
        this.previewImageUrl = previewImageUrl;
        this.partnersImageUrl = partnersImageUrl;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.urlToPlatform = urlToPlatform;
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public Boolean isActive() { return isActive; }

    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getPreviewImageUrl() { return previewImageUrl; }

    public void setPreviewImageUrl(String previewImageUrl) { this.previewImageUrl = previewImageUrl; }

    public String getPartnersImageUrl() { return partnersImageUrl; }

    public void setPartnersImageUrl(String partnersImageUrl) { this.partnersImageUrl = partnersImageUrl; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getEventDate() { return eventDate; }

    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String getUrlToPlatform() { return urlToPlatform; }

    public void setUrlToPlatform(String urlToPlatform) { this.urlToPlatform = urlToPlatform; }
}