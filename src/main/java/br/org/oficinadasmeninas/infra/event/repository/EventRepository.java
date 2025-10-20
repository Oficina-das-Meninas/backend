package br.org.oficinadasmeninas.infra.event.repository;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
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
    public PageDTO<Event> getFiltered(GetEventDto getEventDto) {
        List<Event> events = jdbc.query(
                EventQueryBuilder.GET_FILTERED_EVENTS,
                this::mapRow,
                getEventDto.title(),
                getEventDto.description(),
                getEventDto.location(),
                getEventDto.startDate(),
                getEventDto.endDate(),
                getEventDto.pageSize(),
                getEventDto.page()
        );

        int total = events.size();
        int totalPages = Math.toIntExact((total / getEventDto.pageSize()) + (total % getEventDto.pageSize() == 0 ? 0 : 1));

        return new PageDTO<>(events, total, totalPages);
    }

    @Override
    public Optional<Event> getById(UUID id) {
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

    public UUID create(CreateEventDto createEventDto, String previewFileName, String partnersFileName) {
        var id = UUID.randomUUID();

        jdbc.update(EventQueryBuilder.CREATE_EVENT,
            id,
            createEventDto.title(),
            previewFileName,
            partnersFileName,
            createEventDto.description(),
            Timestamp.valueOf(createEventDto.eventDate()),
            createEventDto.location(),
            createEventDto.urlToPlatform());

        return id;
    }

    @Override
    public void update(UpdateEventDto updateEventDto, String previewFileName, String partnersFileName) {
        jdbc.update(EventQueryBuilder.UPDATE_EVENT,
                updateEventDto.title(),
                previewFileName,
                partnersFileName,
                updateEventDto.description(),
                Timestamp.valueOf(updateEventDto.eventDate()),
                updateEventDto.location(),
                updateEventDto.urlToPlatform(),
                updateEventDto.isActive() == null || updateEventDto.isActive(),
                updateEventDto.id());
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