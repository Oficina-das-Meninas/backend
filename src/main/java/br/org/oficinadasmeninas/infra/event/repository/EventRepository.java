package br.org.oficinadasmeninas.infra.event.repository;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
import br.org.oficinadasmeninas.domain.event.repository.IEventRepository;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EventRepository implements IEventRepository {
    private final JdbcTemplate jdbc;

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public PageDTO<Event> findByFilter(GetEventDto getEventDto) {

        var events = jdbc.query(
                EventQueryBuilder.GET_FILTERED_EVENTS,
                this::mapRow,
                getEventDto.searchTerm(),
                getEventDto.searchTerm(),
                getEventDto.description(),
                getEventDto.startDate(),
                getEventDto.endDate(),
                getEventDto.pageSize(),
                getEventDto.page() * getEventDto.pageSize()
        );

        var total = jdbc.queryForObject(
                EventQueryBuilder.SELECT_COUNT,
                Integer.class,
                getEventDto.searchTerm(),
                getEventDto.searchTerm(),
                getEventDto.description(),
                getEventDto.startDate(),
                getEventDto.endDate());

        if (total == null) total = 0;

        int totalPages = Math.toIntExact((total / getEventDto.pageSize()) + (total % getEventDto.pageSize() == 0 ? 0 : 1));

        return new PageDTO<>(events, total, totalPages);
    }

    @Override
    public Optional<Event> findById(UUID id) {
        try {
            var event = jdbc.queryForObject(EventQueryBuilder.GET_EVENT_BY_ID, this::mapRow, id);
            return Optional.ofNullable(event);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Event insert(Event event) {
        var id = UUID.randomUUID();
        event.setId(id);

        jdbc.update(
                EventQueryBuilder.CREATE_EVENT,
                event.getId(),
                event.getTitle(),
                event.getPreviewImageUrl(),
                event.getPartnersImageUrl(),
                event.getDescription(),
                Timestamp.valueOf(event.getEventDate()),
                event.getLocation(),
                event.getUrlToPlatform()
        );

        return event;
    }

    @Override
    public Event update(Event event, boolean isActive) {

        jdbc.update(EventQueryBuilder.UPDATE_EVENT,
                event.getTitle(),
                event.getPreviewImageUrl(),
                event.getPartnersImageUrl(),
                event.getDescription(),
                Timestamp.valueOf(event.getEventDate()),
                event.getLocation(),
                event.getUrlToPlatform(),
                isActive,
                event.getId()
        );

        return event;
    }

    private Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Event(
                rs.getObject("id", java.util.UUID.class),
                rs.getString("title"),
                rs.getString("preview_image_url"),
                rs.getString("partners_image_url"),
                rs.getString("description"),
                rs.getObject("event_date", LocalDateTime.class),
                rs.getString("location"),
                rs.getString("url_to_platform")
        );
    }
}