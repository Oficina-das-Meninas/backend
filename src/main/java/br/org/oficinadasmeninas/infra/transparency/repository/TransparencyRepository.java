package br.org.oficinadasmeninas.infra.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorDto;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;
import br.org.oficinadasmeninas.domain.transparency.repository.ITransparencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.org.oficinadasmeninas.infra.transparency.repository.TransparencyQueryBuilder.EXISTS_CATEGORY_BY_ID;

@Repository
public class TransparencyRepository implements ITransparencyRepository {

    private final JdbcTemplate jdbc;

    @Autowired
    public TransparencyRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public UUID insertDocument(CreateDocumentDto request) {
        var id = UUID.randomUUID();

        jdbc.update(TransparencyQueryBuilder.INSERT_DOCUMENT,
                id,
                request.title(),
                request.categoryId(),
                new java.sql.Date(request.effectiveDate().getTime()),
                request.previewLink()
        );

        return id;
    }

    @Override
    public UUID insertCollaborator(CreateCollaboratorDto request) {
        var id = UUID.randomUUID();

        jdbc.update(TransparencyQueryBuilder.INSERT_COLLABORATOR,
                id,
                request.image(),
                request.categoryId(),
                request.name(),
                request.role(),
                request.description(),
                request.priority() != null ? request.priority() : 0
        );

        return id;
    }

    @Override
    public Category insertCategory(Category category) {
        var id = UUID.randomUUID();
        category.setId(id);

        jdbc.update(TransparencyQueryBuilder.INSERT_CATEGORY,
            id,
            category.getName(),
            category.getImage(),
            category.getPriority() != null ? category.getPriority() : 0
        );

        return category;
    }

    @Override
    public Optional<Category> findCategoryById(UUID id) {

        try {
            var category = jdbc.queryForObject(TransparencyQueryBuilder.GET_CATEGORY_BY_ID, this::mapRowCategory, id);
            return Optional.ofNullable(category);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Category> findAllCategories() {
        return jdbc.query(TransparencyQueryBuilder.GET_CATEGORIES_ALL, this::mapRowCategory);
    }

    @Override
    public Category updateCategory(Category category) {
        jdbc.update(
            TransparencyQueryBuilder.UPDATE_CATEGORY,
            category.getName(),
            category.getPriority() != null ? category.getPriority() : 0,
            category.getId()
        );
        return category;
    }

    @Override
    public void deleteCategory(UUID id) {
        jdbc.update(TransparencyQueryBuilder.DELETE_CATEGORY, id);
    }

    @Override
    public boolean existsCategoryById(UUID id) {
        return Boolean.TRUE.equals(
            jdbc.queryForObject(EXISTS_CATEGORY_BY_ID, Boolean.class, id)
        );
    }

    @Override
    public int countDocumentsByCategoryId(UUID id) {
        Integer count = jdbc.queryForObject(TransparencyQueryBuilder.COUNT_DOCUMENTS_BY_CATEGORY, Integer.class, id);
        return count == null ? 0 : count;
    }

    @Override
    public int countCollaboratorsByCategoryId(UUID id) {
        Integer count = jdbc.queryForObject(TransparencyQueryBuilder.COUNT_COLLABORATORS_BY_CATEGORY, Integer.class, id);
        return count == null ? 0 : count;
    }


    private Category mapRowCategory(ResultSet rs, int rowNum) throws SQLException {
        Category category = new Category();
        category.setId(rs.getObject("id", UUID.class));
        category.setName(rs.getString("name"));
        category.setImage(rs.getBoolean("is_image"));
        category.setPriority(rs.getInt("priority"));
        return category;
    }
}