package br.org.oficinadasmeninas.domain.partner;

import java.util.UUID;

public class Partner {
    private UUID id;
    private String previewImageUrl;
    private String name;

    public Partner() {}

    public Partner(UUID id, String previewImageUrl, String name) {
        this.id = id;
        this.previewImageUrl = previewImageUrl;
        this.name = name;
    }

    public UUID getId(){
        return this.id;
    }

    public String getPreviewImageUrl(){
        return this.previewImageUrl;
    }

    public String getName(){
        return this.name;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public void setPreviewImageUrl(String previewImageUrl){
        this.previewImageUrl = previewImageUrl;
    }

    public void setName(String name){
        this.name = name;
    }
}
