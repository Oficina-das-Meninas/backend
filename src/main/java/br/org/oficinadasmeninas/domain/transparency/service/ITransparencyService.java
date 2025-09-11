package br.org.oficinadasmeninas.domain.transparency.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

public interface ITransparencyService {

    void uploadDocument(MultipartFile file, String title, Date effectiveDate, String categoryId) throws IOException;
}