package br.org.oficinadasmeninas.domain.transparency.mapper;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.dto.RequestCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.ResponseCategoryDto;

import java.util.UUID;

public class CategoryMapper {

    public static ResponseCategoryDto toDto(Category category) {
        return new ResponseCategoryDto(
            category.getId(),
            category.getName(),
            category.getIsImage(),
            category.getPriority()
        );
    }

    public static Category toEntity(RequestCategoryDto dto) {
        return new Category(
            UUID.randomUUID(),
            dto.name(),
            dto.isImage(),
            dto.priority()
        );
    }
}
