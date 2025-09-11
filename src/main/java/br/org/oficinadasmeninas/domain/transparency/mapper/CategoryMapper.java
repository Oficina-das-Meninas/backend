package br.org.oficinadasmeninas.domain.transparency.mapper;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.ResponseCategoryDto;
import br.org.oficinadasmeninas.domain.transparency.dto.UpdateCategoryDto;

public class CategoryMapper {

    public static ResponseCategoryDto toDto(Category category) {
        return new ResponseCategoryDto(
            category.getId(),
            category.getName(),
            category.getImage(),
            category.getPriority()
        );
    }

    public static Category toEntity(CreateCategoryDto dto) {
        return new Category(
            dto.name(),
            dto.isImage(),
            dto.priority()
        );
    }

}
