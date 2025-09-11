package br.org.oficinadasmeninas.infra.event.repository;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.repository.IEventRepository;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EventRepository implements IEventRepository {
    private final JdbcTemplate jdbc;

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public PageDTO<Event> findAll(int page, int pageSize) {
        String rowCountSql = "select count(*) from events";
        int total = jdbc.queryForObject(rowCountSql, Integer.class);
        int totalPages = Math.toIntExact((total / pageSize) + (total % pageSize == 0 ? 0 : 1));

        String querySql = """
	        select id
	              ,title
	              ,preview_image_url
                  ,description
                  ,amount
                  ,event_date
                  ,location
                  ,url_to_platform
	        from events
	        order BY id
	       limit ? offset ?
	    """;

        List<Event> events = jdbc.query(
                querySql,
                this::mapRow,
                pageSize,
                page
        );

        return new PageDTO<>(events, total, totalPages);
    }

    private Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Event(
                rs.getObject("id", java.util.UUID.class),
                rs.getString("title"),
                rs.getString("preview_image_url"),
                rs.getString("description"),
                rs.getBigDecimal("amount"),
                rs.getObject("event_date", LocalDateTime.class),
                rs.getString("location"),
                rs.getString("url_to_platform")
        );
    }
}