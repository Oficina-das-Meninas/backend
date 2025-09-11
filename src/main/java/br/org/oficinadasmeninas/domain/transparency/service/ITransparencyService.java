package br.org.oficinadasmeninas.domain.transparency.service;

import br.org.oficinadasmeninas.domain.transparency.dto.CreateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.ResponseCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.GetCategoriesResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ITransparencyService {

    void uploadDocument(MultipartFile file, String title, Date effectiveDate, String categoryId) throws IOException;
    void uploadCollaborator(MultipartFile file, String name, String role, String description, String priority, String categoryId) throws IOException;
  
    void deleteCollaborator(UUID id) throws IOException;
    void deleteDocument(UUID id) throws IOException;
  
    ResponseCategoryDto insertCategory(CreateCategoryDto request);

    ResponseCategoryDto getCategoryById(UUID id);
    List<ResponseCategoryDto> getAllCategories();
    GetCategoriesResponseDto getAllCategoriesWithDocuments();

    ResponseCategoryDto updateCategory(UUID id, UpdateCategoryDto request);
    void deleteCategory(UUID id);
}