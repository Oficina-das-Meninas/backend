package br.org.oficinadasmeninas.infra.partner.repository;

public class PartnerQueryBuilder {
    public static final String SELECT_COUNT = """
        SELECT count(*)
        FROM partners
        WHERE active
    """;

    public static final String GET_PARTNERS = """
        SELECT id, preview_image_url, name
        FROM partners
        WHERE active
        ORDER BY id
        LIMIT ? OFFSET ?
    """;

    public static final String GET_PARTNER_BY_ID = """
        SELECT id, preview_image_url, name
        FROM partners
        WHERE id = ?
          AND active
    """;

    public static final String CREATE_PARTNER = """
        INSERT INTO partners (id, preview_image_url, name)
        VALUES (?, ?, ?)
    """;

    public static final String UPDATE_PARTNER = """
        UPDATE partners SET
            preview_image_url = ?,
            name = ?,
            active = ?
        WHERE id = ?
    """;
}