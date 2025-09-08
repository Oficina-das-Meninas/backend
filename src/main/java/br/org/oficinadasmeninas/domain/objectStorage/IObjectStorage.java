package br.org.oficinadasmeninas.domain.objectStorage;

import org.springframework.web.multipart.MultipartFile;

public interface IObjectStorage {
    void upload(MultipartFile file);
    void upload(MultipartFile file, String fileName);
    MultipartFile getFile(String fileName);
}
