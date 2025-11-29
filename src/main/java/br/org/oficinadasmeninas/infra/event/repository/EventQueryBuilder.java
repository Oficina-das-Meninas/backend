package br.org.oficinadasmeninas.infra.event.repository;

public class EventQueryBuilder {
    public static final String SELECT_COUNT = """
        SELECT count(*)
        FROM EVENTS
        WHERE (
                (unaccent(title) ILIKE unaccent(COALESCE('%' || ? || '%', title)))
                OR (unaccent(location) ILIKE unaccent(COALESCE('%' || ? || '%', location)))
          )
          AND description ILIKE COALESCE('%' || ? || '%', description)
          AND event_date BETWEEN COALESCE(?, event_date)
                         AND COALESCE(?, event_date)
    """;

    public static final String GET_EVENT_BY_ID = """
        SELECT id, title, preview_image_url, partners_image_url, description, event_date, location, url_to_platform
        FROM EVENTS
        WHERE id = ?
    """;

    public static final String GET_FILTERED_EVENTS = """
        SELECT id, title, preview_image_url, partners_image_url, description, event_date, location,  url_to_platform
        FROM EVENTS
        WHERE (
                (unaccent(title) ILIKE unaccent(COALESCE('%' || ? || '%', title)))
                OR (unaccent(location) ILIKE unaccent(COALESCE('%' || ? || '%', location)))
          )
          AND description ILIKE COALESCE('%' || ? || '%', description)
          AND event_date BETWEEN COALESCE(?, event_date)
                         AND COALESCE(?, event_date)
        ORDER BY event_date DESC
        LIMIT ? OFFSET ?;
    """;

    public static final String CREATE_EVENT = """
        INSERT INTO EVENTS (id, title, preview_image_url, partners_image_url, description, event_date, location, url_to_platform)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String UPDATE_EVENT = """
        UPDATE EVENTS SET
            title = ?,
            preview_image_url = ?,
            partners_image_url = ?,
            description = ?,
            event_date = ?,
            location = ?,
            url_to_platform = ?
        WHERE id = ?
    """;

    public static final String DELETE_EVENT = """
    		DELETE FROM EVENTS
    		WHERE id = ?
    """;
}