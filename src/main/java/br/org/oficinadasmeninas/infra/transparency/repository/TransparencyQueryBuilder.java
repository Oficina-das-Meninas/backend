package br.org.oficinadasmeninas.infra.transparency.repository;

public class TransparencyQueryBuilder {

    public static final String INSERT_DOCUMENT = """
            INSERT INTO documents (id, title, category_id, effective_date, preview_link)
            VALUES (?, ?, ?, ?, ?)
        """;

    public static final String GET_CATEGORY_BY_ID = """
        SELECT id,
               name,
               is_image,
               priority
        FROM categories
        WHERE id = ?
    """;

    public static final String GET_ALL_CATEGORIES = "SELECT id, name, is_image, priority FROM categories";

    public static final String GET_ALL_DOCUMENTS = "SELECT id, title, effective_date, category_id, preview_link FROM documents";

    public static final String GET_ALL_COLLABORATORS = "SELECT id, preview_image_url, name, role, description, priority, category_id FROM collaborators";
}
