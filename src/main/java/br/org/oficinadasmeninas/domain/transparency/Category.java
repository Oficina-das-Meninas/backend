package br.org.oficinadasmeninas.domain.transparency;

import java.util.UUID;

public class Category {
    private UUID id;
    private String name;
    private Boolean isImage;
    private Integer priority;

    public Category(UUID id, String name, Boolean isImage, Integer priority) {
        this.id = id;
        this.name = name;
        this.isImage = isImage;
        this.priority = priority;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsImage() {
        return isImage;
    }

    public Integer getPriority() {
        return priority;
    }
}
