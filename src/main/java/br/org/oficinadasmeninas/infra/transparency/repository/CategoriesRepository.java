package br.org.oficinadasmeninas.infra.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.repository.ICategoriesRepository;
import br.org.oficinadasmeninas.infra.transparency.repository.queries.CategoriesQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoriesRepository implements ICategoriesRepository {

    private final JdbcTemplate jdbc;

    @Autowired
    public CategoriesRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Category insert(Category category) {
        var id = UUID.randomUUID();
        category.setId(id);

        jdbc.update(CategoriesQueryBuilder.INSERT_CATEGORY,
                id,
                category.getName(),
                category.getImage(),
                category.getPriority() != null ? category.getPriority() : 0
        );

        return category;
    }

    @Override
    public Category update(Category category) {
        jdbc.update(
                CategoriesQueryBuilder.UPDATE_CATEGORY,
                category.getName(),
                category.getPriority() != null ? category.getPriority() : 0,
                category.getId()
        );
        return category;
    }

    @Override
    public void delete(UUID id) {
        jdbc.update(CategoriesQueryBuilder.DELETE_CATEGORY, id);
    }


    @Override
    public boolean existsById(UUID id) {
        return Boolean.TRUE.equals(
                jdbc.queryForObject(CategoriesQueryBuilder.EXISTS_CATEGORY_BY_ID, Boolean.class, id)
        );
    }

    @Override
    public Optional<Category> findById(UUID id) {

        try {
            var category = jdbc.queryForObject(CategoriesQueryBuilder.GET_CATEGORY_BY_ID, this::mapRowCategory, id);
            return Optional.ofNullable(category);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Category> findAll() {
        return jdbc.query(CategoriesQueryBuilder.GET_CATEGORIES_ALL, this::mapRowCategory);
    }

    private Category mapRowCategory(ResultSet rs, int rowNum) throws SQLException {
        var category = new Category();
        category.setId(rs.getObject("id", UUID.class));
        category.setName(rs.getString("name"));
        category.setImage(rs.getBoolean("is_image"));
        category.setPriority(rs.getInt("priority"));
        return category;
    }
}
