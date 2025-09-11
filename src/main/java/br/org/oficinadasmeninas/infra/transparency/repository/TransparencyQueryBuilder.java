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
}
