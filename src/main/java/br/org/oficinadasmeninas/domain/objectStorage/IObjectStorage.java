package br.org.oficinadasmeninas.domain.objectStorage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IObjectStorage {
    void upload(MultipartFile file, Boolean isPublic) throws IOException;
    void upload(MultipartFile file, String fileName, Boolean isPublic) throws IOException;
}