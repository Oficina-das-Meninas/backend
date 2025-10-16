package br.org.oficinadasmeninas.domain.transparency.service;

import br.org.oficinadasmeninas.domain.transparency.dto.CreateCategoryRequestDto;
import br.org.oficinadasmeninas.domain.transparency.dto.ResponseCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.getCategories.GetCategoriesResponseDto;

import java.util.List;
import java.util.UUID;

public interface ICategoriesService {

    UUID insertCategory(CreateCategoryRequestDto request);

    UUID updateCategory(UUID id, UpdateCategoryDto request);

    UUID deleteCategory(UUID id);

    ResponseCategoryDto getCategoryById(UUID id);

    List<ResponseCategoryDto> getAllCategories();

    GetCategoriesResponseDto getAllCategoriesWithDocuments();
}
