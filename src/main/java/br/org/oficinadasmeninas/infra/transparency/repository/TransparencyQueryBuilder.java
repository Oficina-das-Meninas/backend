package br.org.oficinadasmeninas.infra.transparency.repository;

public class TransparencyQueryBuilder {

    public static final String INSERT_DOCUMENT = """
            INSERT INTO documents (id, title, category_id, effective_date, preview_link)
            VALUES (?, ?, ?, ?, ?)
        """;

    public static final String INSERT_CATEGORY = """
        INSERT INTO categories(
            id, name, is_image, priority
        ) VALUES (?, ?, ?, ?)
    """;

    public static final String GET_CATEGORY_BY_ID = """
        SELECT id,
               name,
               is_image,
               priority
        FROM categories
        WHERE id = ?
    """;

    public static final String GET_CATEGORIES_ALL = """
        SELECT * FROM categories ORDER BY priority
    """;

    public static final String UPDATE_CATEGORY = """
        UPDATE categories SET
            name = ?,
            priority = ?
        WHERE id = ?
    """;

    public static final String DELETE_CATEGORY = """
        DELETE FROM categories WHERE id = ?
    """;

    public static final String EXISTS_CATEGORY_BY_ID = """
        SELECT EXISTS(SELECT 1 FROM categories WHERE id = ?)
    """;

    public static final String COUNT_DOCUMENTS_BY_CATEGORY = """
        SELECT COUNT(*) FROM documents WHERE category_id = ?
    """;

    public static final String COUNT_COLLABORATORS_BY_CATEGORY = """
        SELECT COUNT(*) FROM collaborators WHERE category_id = ?
    """;
}
