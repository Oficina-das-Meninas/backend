package br.org.oficinadasmeninas.infra.event.repository;

public class EventQueryBuilder {

    public static final String SELECT_COUNT = """
        SELECT count(*)
        FROM events
    """;

    public static final String GET_ALL_EVENTS = """
        SELECT id, title, preview_image_url, description, amount, event_date, location, url_to_platform
        FROM EVENTS
        ORDER BY id
        LIMIT ? OFFSET ?
    """;

    public static final String CREATE_EVENT = """
        INSERT INTO events (id, title, preview_image_url, description, amount, event_date, location, url_to_platform)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String UPDATE_EVENT = """
        UPDATE events SET
            title = ?,
            preview_image_url = ?,
            description = ?,
            amount = ?,
            event_date = ?,
            location = ?,
            url_to_platform
        WHERE id = ?
    """;
}