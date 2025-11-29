package br.org.oficinadasmeninas.infra.transparency.repository.queries;

public class DocumentsQueryBuilder {

    public static final String INSERT_DOCUMENT = """
        INSERT INTO documents (id, title, category_id, effective_date, preview_link)
        VALUES (?, ?, ?, ?, ?)
    """;

    public static final String DELETE_DOCUMENT = """
        DELETE FROM documents
        WHERE id = ?;
    """;

    public static final String COUNT_DOCUMENTS_BY_CATEGORY = """
        SELECT COUNT(*) FROM documents WHERE category_id = ?
    """;

    public static final String GET_DOCUMENT_BY_ID = """
             SELECT id,
                    title,
                    effective_date,
                    category_id,
                    preview_link
             FROM documents
             WHERE id = ?
        """;

    public static final String GET_ALL_DOCUMENTS = """
        SELECT id, title, effective_date, category_id, preview_link
        FROM documents
    """;
}
