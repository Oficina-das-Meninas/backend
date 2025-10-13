package br.org.oficinadasmeninas.infra.event.repository;

public class EventQueryBuilder {
    public static final String GET_EVENT_BY_ID = """
        SELECT id, title, preview_image_url, partners_image_url, description, event_date, location, url_to_platform
        FROM EVENTS
        WHERE id = ?
          AND active
    """;

    public static final String GET_FILTERED_EVENTS = """
            SELECT id, title, preview_image_url, partners_image_url, description, event_date, location,  url_to_platform
            FROM EVENTS
            WHERE (title ILIKE COALESCE('%' || ? || '%', title))
              AND (description ILIKE COALESCE('%' || ? || '%', description))
              AND (location ILIKE COALESCE('%' || ? || '%', location))
              AND event_date BETWEEN COALESCE(?, event_date)
                             AND COALESCE(?, event_date)
              AND active
            ORDER BY event_date DESC
            LIMIT ? OFFSET ?;
    """;

    public static final String CREATE_EVENT = """
        INSERT INTO events (id, title, preview_image_url, partners_image_url, description, event_date, location, url_to_platform)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String UPDATE_EVENT = """
        UPDATE events SET
            title = ?,
            preview_image_url = ?,
            partners_image_url = ?,
            description = ?,
            event_date = ?,
            location = ?,
            url_to_platform = ?,
            active = ?
        WHERE id = ?
    """;
}