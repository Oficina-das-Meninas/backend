package br.org.oficinadasmeninas.infra.transparency.repository;

public class TransparencyQueryBuilder {

    public static final String INSERT_DOCUMENT = """
            INSERT INTO documents (id, title, category_id, effective_date, preview_link)
            VALUES (?, ?, ?, ?, ?)
        """;

    public static final String INSERT_COLLABORATOR = """
        INSERT INTO collaborators 
            (id, preview_image_url, category_id, name, role, description, priority)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

    public static final String GET_CATEGORY_BY_ID = """
        SELECT id,
               name,
               is_image,
               priority
        FROM categories
        WHERE id = ?
    """;

    public static final String DELETE_COLLABORATOR = """
        DELETE FROM collaborators
        WHERE id = ?;
    """;

    public static final String GET_COLLABORATOR_BY_ID = """
             SELECT id, 
                    preview_image_url, 
                    category_id, 
                    name, 
                    role, 
                    description, 
                    priority 
             FROM collaborators 
             WHERE id = ?
            """;
}
