package br.org.oficinadasmeninas.infra.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorDto;
import br.org.oficinadasmeninas.domain.transparency.Collaborator;
import br.org.oficinadasmeninas.domain.transparency.Document;
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
        return jdbc.query(TransparencyQueryBuilder.GET_ALL_CATEGORIES, this::mapRowCategory);
    }

    @Override
    public List<Document> findAllDocuments() {
        return jdbc.query(TransparencyQueryBuilder.GET_ALL_DOCUMENTS, this::mapRowDocument);
    }

    @Override
    public List<Collaborator> findAllCollaborators() {
        return jdbc.query(TransparencyQueryBuilder.GET_ALL_COLLABORATORS, this::mapRowCollaborator);
    }

    private Category mapRowCategory(ResultSet rs, int rowNum) throws SQLException {
        var category = new Category();
        category.setId(rs.getObject("id", UUID.class));
        category.setName(rs.getString("name"));
        category.setImage(rs.getBoolean("is_image"));
        category.setPriority(rs.getInt("priority"));
        return category;
    }

    private Document mapRowDocument(ResultSet rs, int rowNum) throws SQLException {
        var document = new Document();
        document.setId(rs.getObject("id", UUID.class));
        document.setTitle(rs.getString("title"));
        document.setEffectiveDate(rs.getDate("effective_date"));
        document.setPreviewLink(rs.getString("preview_link"));

        var category = new Category();
        category.setId(rs.getObject("category_id", UUID.class));
        document.setCategory(category);

        return document;
    }

    private Collaborator mapRowCollaborator(ResultSet rs, int rowNum) throws SQLException {
        var collaborator = new Collaborator();
        collaborator.setId(rs.getObject("id", UUID.class));
        collaborator.setImage(rs.getString("preview_image_url"));
        collaborator.setName(rs.getString("name"));
        collaborator.setRole(rs.getString("role"));
        collaborator.setDescription(rs.getString("description"));
        collaborator.setPriority(rs.getInt("priority"));

        var category = new Category();
        category.setId(rs.getObject("category_id", UUID.class));
        collaborator.setCategory(category);

        return collaborator;
    }
}