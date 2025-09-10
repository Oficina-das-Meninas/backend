package br.org.oficinadasmeninas.domain.transparency.service;

import br.org.oficinadasmeninas.domain.transparency.dto.RequestCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.ResponseCategoryDto;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    ResponseCategoryDto createCategory(RequestCategoryDto request);
    ResponseCategoryDto updateCategory(UUID id, RequestCategoryDto request);
    void deleteCategory(UUID id);
    ResponseCategoryDto findById(UUID id);
    List<ResponseCategoryDto> findAll();
}
