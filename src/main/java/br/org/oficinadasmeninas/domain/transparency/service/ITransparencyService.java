package br.org.oficinadasmeninas.domain.transparency.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public interface ITransparencyService {

    void uploadDocument(MultipartFile file, String title, Date effectiveDate, String categoryId) throws IOException;
    void uploadCollaborator(MultipartFile file, String name, String role, String description, String priority, String categoryId) throws IOException;
    void deleteCollaborator(UUID id) throws IOException;
    void deleteDocument(UUID id) throws IOException;
}