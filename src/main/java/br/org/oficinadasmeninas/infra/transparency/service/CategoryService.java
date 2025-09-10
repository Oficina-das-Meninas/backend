package br.org.oficinadasmeninas.infra.transparency.service;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.dto.RequestCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.ResponseCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.domain.transparency.mapper.CategoryMapper;
import br.org.oficinadasmeninas.domain.transparency.repository.ICategoryRepository;
import br.org.oficinadasmeninas.domain.transparency.service.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ResponseCategoryDto createCategory(RequestCategoryDto request) {
        Category entity = categoryRepository.save(CategoryMapper.toEntity(request));

        return CategoryMapper.toDto(entity);
    }

    @Override
    public ResponseCategoryDto updateCategory(UUID id, RequestCategoryDto request) {
        getCategoryOrThrow(id);

        Category entity = categoryRepository.update(CategoryMapper.toEntity(request));

        return CategoryMapper.toDto(entity);
    }

    @Override
    public void deleteCategory(UUID id) {
        getCategoryOrThrow(id);

        categoryRepository.delete(id);
    }

    @Override
    public ResponseCategoryDto findById(UUID id) {
        Category entity = getCategoryOrThrow(id);

        return CategoryMapper.toDto(entity);
    }

    @Override
    public List<ResponseCategoryDto> findAll() {
        return categoryRepository.findAll().stream()
            .map(CategoryMapper::toDto)
            .toList();
    }

    private Category getCategoryOrThrow(UUID id) {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        return category;
    }

}
