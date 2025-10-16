package br.org.oficinadasmeninas.infra.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.dto.CreateDocumentDto;
import br.org.oficinadasmeninas.domain.transparency.repository.ICategoriesRepository;
import br.org.oficinadasmeninas.domain.transparency.repository.IDocumentsRepository;
import br.org.oficinadasmeninas.infra.transparency.repository.queries.DocumentsQueryBuilder;
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
public class DocumentsRepository implements IDocumentsRepository {


    private final JdbcTemplate jdbc;
    private final ICategoriesRepository categoriesRepository;

    @Autowired
    public DocumentsRepository(JdbcTemplate jdbc, ICategoriesRepository categoriesRepository) {
        this.jdbc = jdbc;
        this.categoriesRepository = categoriesRepository;
    }


    @Override
    public UUID insert(CreateDocumentDto request) {
        var id = UUID.randomUUID();

        jdbc.update(DocumentsQueryBuilder.INSERT_DOCUMENT,
                id,
                request.title(),
                request.categoryId(),
                new java.sql.Date(request.effectiveDate().getTime()),
                request.previewLink()
        );

        return id;
    }

    @Override
    public void delete(UUID id) {
        jdbc.update(DocumentsQueryBuilder.DELETE_DOCUMENT, id);
    }


    @Override
    public int countByCategoryId(UUID id) {
        Integer count = jdbc.queryForObject(DocumentsQueryBuilder.COUNT_DOCUMENTS_BY_CATEGORY, Integer.class, id);
        return count == null ? 0 : count;
    }

    @Override
    public Optional<Document> findById(UUID id) {
        try {
            var document = jdbc.queryForObject(DocumentsQueryBuilder.GET_DOCUMENT_BY_ID, this::mapRowDocument, id);
            return Optional.ofNullable(document);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Document> findAll() {
        return jdbc.query(DocumentsQueryBuilder.GET_ALL_DOCUMENTS, this::mapRowDocument);
    }

    private Document mapRowDocument(ResultSet rs, int rowNum) throws SQLException {
        Document document = new Document();
        document.setId(rs.getObject("id", UUID.class));
        document.setTitle(rs.getString("title"));
        document.setEffectiveDate(rs.getDate("effective_date"));
        document.setPreviewLink(rs.getString("preview_link"));

        UUID categoryId = rs.getObject("category_id", UUID.class);
        Category category = categoriesRepository.findById(categoryId)
                .orElse(null);
        document.setCategory(category);

        return document;
    }
}
