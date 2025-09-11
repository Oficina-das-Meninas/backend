package br.org.oficinadasmeninas.domain.transparency;

import java.util.UUID;

public class Category {
    private UUID id;
    private String name;
    private Boolean isImage;
    private Integer priority;

    public Category() {
    }

    public Category(String name, Boolean isImage, Integer priority) {
        this.name = name;
        this.isImage = isImage;
        this.priority = priority;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getImage() {
        return isImage;
    }

    public void setImage(Boolean image) {
        isImage = image;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
