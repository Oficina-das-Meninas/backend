package br.org.oficinadasmeninas.infra.transparency.repository;

import br.org.oficinadasmeninas.domain.transparency.Category;
import br.org.oficinadasmeninas.domain.transparency.Document;
import br.org.oficinadasmeninas.domain.transparency.repository.ICategoriesRepository;
import br.org.oficinadasmeninas.domain.transparency.repository.IDocumentsRepository;
import br.org.oficinadasmeninas.infra.transparency.repository.queries.DocumentsQueryBuilder;
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

    public DocumentsRepository(JdbcTemplate jdbc, ICategoriesRepository categoriesRepository) {
        this.jdbc = jdbc;
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public Document insert(Document document) {

        var id = UUID.randomUUID();
        document.setId(id);

        java.sql.Date sqlDate = document.getEffectiveDate() == null
                ? null
                : new java.sql.Date(document.getEffectiveDate().getTime());

        jdbc.update(DocumentsQueryBuilder.INSERT_DOCUMENT,
                id,
                document.getTitle(),
                document.getCategory().getId(),
                sqlDate,
                document.getPreviewLink()
        );

        return document;
    }

    @Override
    public void deleteById(UUID id) {
        jdbc.update(DocumentsQueryBuilder.DELETE_DOCUMENT, id);
    }

    @Override
    public List<Document> findAll() {
        return jdbc.query(
                DocumentsQueryBuilder.GET_ALL_DOCUMENTS,
                this::mapRowDocument
        );
    }

    @Override
    public Optional<Document> findById(UUID id) {
        return jdbc.query(
                DocumentsQueryBuilder.GET_DOCUMENT_BY_ID,
                this::mapRowDocument,
                id
        ).stream().findFirst();
    }

    @Override
    public int countByCategoryId(UUID id) {
        var count = jdbc.queryForObject(
                DocumentsQueryBuilder.COUNT_DOCUMENTS_BY_CATEGORY,
                Integer.class,
                id
        );

        return count == null ? 0 : count;
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
