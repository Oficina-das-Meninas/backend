package br.org.oficinadasmeninas.domain.transparency.service;

import br.org.oficinadasmeninas.domain.transparency.dto.*;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.GetCategoriesResponseDto;

import java.util.List;
import java.util.UUID;

public interface ITransparencyService {

    UUID insertCategory(CreateCategoryRequestDto request);

    UUID uploadDocument(CreateDocumentRequestDto request);

    UUID uploadCollaborator(CreateCollaboratorRequestDto request);

    UUID updateCategory(UUID id, UpdateCategoryDto request);

    UUID deleteCollaborator(UUID id);

    UUID deleteDocument(UUID id);

    UUID deleteCategory(UUID id);

    ResponseCategoryDto getCategoryById(UUID id);

    List<ResponseCategoryDto> getAllCategories();

    GetCategoriesResponseDto getAllCategoriesWithDocuments();
}