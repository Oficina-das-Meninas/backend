package br.org.oficinadasmeninas.infra.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;
import br.org.oficinadasmeninas.domain.transparency.repository.ITransparencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

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
    public Optional<Category> findCategoryById(UUID id) {

        try {
            var category = jdbc.queryForObject(TransparencyQueryBuilder.GET_CATEGORY_BY_ID, this::mapRowCategory, id);
            return Optional.ofNullable(category);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
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