package br.org.oficinadasmeninas.infra.transparency.repository.queries;

public class CollaboratorsQueryBuilder {

    public static final String INSERT_COLLABORATOR = """
        INSERT INTO collaborators
            (id, preview_image_url, category_id, name, role, description, priority)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String UPDATE_COLLABORATOR_PRIORITY = """
        UPDATE collaborators SET
            priority = ?
        WHERE id = ?
    """;

    public static final String DELETE_COLLABORATOR = """
        DELETE FROM collaborators
        WHERE id = ?;
    """;

    public static final String COUNT_COLLABORATORS_BY_CATEGORY = """
        SELECT COUNT(*) FROM collaborators WHERE category_id = ?
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

    public static final String GET_ALL_COLLABORATORS = """
        SELECT id,
               preview_image_url,
               name,
               role,
               description,
               priority,
               category_id
        FROM collaborators
    """;

}
