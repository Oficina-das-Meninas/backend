package br.org.oficinadasmeninas.infra.event.repository;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.domain.event.repository.IEventRepository;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EventRepository implements IEventRepository {
    private final JdbcTemplate jdbc;

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Event> getEventById(UUID id) {
        try
        {
            var event = jdbc.queryForObject(EventQueryBuilder.GET_EVENT_BY_ID, this::mapRow, id);
            return Optional.of(event);
        }
        catch (EmptyResultDataAccessException e)
        {
            return Optional.empty();
        }
    }

    @Override
    public PageDTO<Event> findAll(int page, int pageSize) {
        String rowCountSql = EventQueryBuilder.SELECT_COUNT;

        int total = jdbc.queryForObject(rowCountSql, Integer.class);
        int totalPages = Math.toIntExact((total / pageSize) + (total % pageSize == 0 ? 0 : 1));

        List<Event> events = jdbc.query(
                EventQueryBuilder.GET_ALL_EVENTS,
                this::mapRow,
                pageSize,
                page
        );

        return new PageDTO<>(events, total, totalPages);
    }

    public UUID createEvent(CreateEventDto createEventDto, String previewFileName, String partnersFileName) {
        var id = UUID.randomUUID();

        jdbc.update(EventQueryBuilder.CREATE_EVENT,
            id,
            createEventDto.title(),
            previewFileName,
            partnersFileName,
            createEventDto.description(),
            createEventDto.amount(),
            Timestamp.valueOf(createEventDto.eventDate()),
            createEventDto.location(),
            createEventDto.urlToPlatform());

        return id;
    }

    @Override
    public void updateEvent(UpdateEventDto updateEventDto, String previewFileName, String partnersFileName) {
        jdbc.update(EventQueryBuilder.UPDATE_EVENT,
                updateEventDto.title(),
                updateEventDto.previewImageUrl(),
                updateEventDto.partnersImageUrl(),
                updateEventDto.description(),
                updateEventDto.amount(),
                Timestamp.valueOf(updateEventDto.eventDate()),
                updateEventDto.location(),
                updateEventDto.urlToPlatform(),
                updateEventDto.id());
    }

    private Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Event(
                rs.getObject("id", java.util.UUID.class),
                rs.getString("title"),
                rs.getString("preview_image_url"),
                rs.getString("partners_image_url"),
                rs.getString("description"),
                rs.getBigDecimal("amount"),
                rs.getObject("event_date", LocalDateTime.class),
                rs.getString("location"),
                rs.getString("url_to_platform")
        );
    }
}