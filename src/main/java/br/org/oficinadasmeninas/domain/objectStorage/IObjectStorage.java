package br.org.oficinadasmeninas.domain.objectStorage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface IObjectStorage {
    String sanitizeFileName(String fileName);
    void upload(String key, InputStream data, String contentType) throws IOException;
    void upload(MultipartFile file, Boolean isPublic) throws IOException;
    void upload(MultipartFile file, String fileName, Boolean isPublic) throws IOException;
    String uploadTransparencyFile(MultipartFile file, boolean isImage) throws  IOException;
    void deleteTransparencyFile(String fileUrl) throws IOException;
}