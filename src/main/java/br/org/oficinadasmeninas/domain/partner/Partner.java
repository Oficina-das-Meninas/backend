package br.org.oficinadasmeninas.domain.partner;

import java.util.UUID;

public class Partner {
    private UUID id;
    private String previewUrl;
    private String name;

    public Partner() {}

    public Partner(UUID id, String previewUrl, String name) {
        this.id = id;
        this.previewUrl = previewUrl;
        this.name = name;
    }

    public UUID getId(){
        return this.id;
    }

    public String getPreviewUrl(){
        return this.previewUrl;
    }

    public String getName(){
        return this.name;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public void setPreviewUrl(String previewUrl){
        this.previewUrl = previewUrl;
    }

    public void setName(String name){
        this.name = name;
    }
}
