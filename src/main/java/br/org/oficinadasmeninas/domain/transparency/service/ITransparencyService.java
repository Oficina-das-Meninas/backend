package br.org.oficinadasmeninas.domain.transparency.service;

import br.org.oficinadasmeninas.domain.Response;
import br.org.oficinadasmeninas.domain.transparency.dto.*;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.GetCategoriesResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ITransparencyService {

    Response<UUID> insertCategory(CreateCategoryRequestDto request);

    Response<UUID> uploadDocument(CreateDocumentRequestDto request) throws IOException;

    Response<UUID> uploadCollaborator(CreateCollaboratorRequestDto request) throws IOException;

    Response<Void> updateCategory(UUID id, UpdateCategoryDto request);

    Response<Void> deleteCollaborator(UUID id) throws IOException;

    Response<Void> deleteDocument(UUID id) throws IOException;

    Response<Void> deleteCategory(UUID id);

    Response<ResponseCategoryDto> getCategoryById(UUID id);

    Response<List<ResponseCategoryDto>> getAllCategories();

    Response<GetCategoriesResponseDto> getAllCategoriesWithDocuments();
}