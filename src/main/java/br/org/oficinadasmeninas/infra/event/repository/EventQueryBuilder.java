package br.org.oficinadasmeninas.infra.event.repository;

public class EventQueryBuilder {

    public static final String GET_ALL_EVENTS = """
        SELECT (id, title, preview_image_url, description, amount, event_date, location, url_to_platform)
        FROM EVENTS
        ORDER BY id
        LIMIT ? OFSET ?
    """;

    public static final String CREATE_EVENT = """
        INSERT INTO events (title, preview_image_url, description, amount, event_date, location, url_to_platform)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;
}