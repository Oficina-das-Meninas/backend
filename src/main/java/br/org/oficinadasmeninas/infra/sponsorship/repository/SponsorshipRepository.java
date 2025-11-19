package br.org.oficinadasmeninas.infra.sponsorship.repository;

import br.org.oficinadasmeninas.domain.sponsorship.Sponsorship;
import br.org.oficinadasmeninas.domain.sponsorship.repository.ISponsorshipRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SponsorshipRepository implements ISponsorshipRepository {
    private final JdbcTemplate jdbc;

    public SponsorshipRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    @Override
    public Sponsorship insert(Sponsorship sponsorship) {
        UUID id = UUID.randomUUID();
        sponsorship.setId(id);

        jdbc.update(
                SponsorshipQueryBuilder.INSERT_SPONSORSHIP,
                id,
                sponsorship.getBillingDay(),
                sponsorship.getStartDate(),
                sponsorship.getIsActive(),
                sponsorship.getSubscriptionId(),
                sponsorship.getUserId(),
                sponsorship.getCancelDate()
        );

        return sponsorship;
    }

    @Override
    public Sponsorship update(Sponsorship sponsorship) {
        jdbc.update(
                SponsorshipQueryBuilder.UPDATE_SPONSORSHIP,
                sponsorship.getCancelDate(),
                sponsorship.getIsActive(),
                sponsorship.getSubscriptionId(),
                sponsorship.getId()
        );

        return sponsorship;
    }

    @Override
    public void activateByUserId(UUID userId) {
        jdbc.update(SponsorshipQueryBuilder.ACTIVATE_SPONSORSHIP_BY_USER_ID, true, userId);
    }

    @Override
    public void cancelById(UUID id) {
        jdbc.update(
                SponsorshipQueryBuilder.CANCEL_SPONSORSHIP_BY_ID,
                LocalDateTime.now(),
                false,
                id
        );
    }

    @Override
    public List<Sponsorship> findAll() {
        return List.of();
    }


    @Override
    public Optional<Sponsorship> findById(UUID id) {

        try {
            var sponsorship = jdbc.queryForObject(
                    SponsorshipQueryBuilder.GET_SPONSORSHIP_BY_ID,
                    this::mapRowSponsorship,
                    id
            );

            return Optional.ofNullable(sponsorship);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Sponsorship> findByUserId(UUID id) {

        return jdbc.query(
                SponsorshipQueryBuilder.GET_SPONSORSHIP_BY_USER_ID,
                this::mapRowSponsorship,
                id
        );
    }

    @Override
    public Optional<Sponsorship> findActiveByUserId(UUID userId) {
        try {
            var sponsorship = jdbc.queryForObject(
                    SponsorshipQueryBuilder.GET_ACTIVE_SPONSORSHIP_BY_USER_ID,
                    this::mapRowSponsorship,
                    userId
            );

            return Optional.ofNullable(sponsorship);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Sponsorship> findBySubscriptionId(String subscriptionId) {
        try {
            var sponsorship = jdbc.queryForObject(
                    SponsorshipQueryBuilder.GET_SPONSORSHIP_BY_SUBSCRIPTION_ID,
                    this::mapRowSponsorship,
                    subscriptionId
            );

            return Optional.ofNullable(sponsorship);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Sponsorship mapRowSponsorship(ResultSet rs, int rowNum) throws SQLException {
        return new Sponsorship(
                UUID.fromString(rs.getString("id")),
                rs.getInt("billing_day"),
                rs.getTimestamp("start_date") != null ? rs.getTimestamp("start_date").toLocalDateTime() : null,
                rs.getBoolean("is_active"),
                rs.getString("subscription_id"),
                UUID.fromString(rs.getString("user_id")),
                rs.getTimestamp("cancel_date") != null ? rs.getTimestamp("cancel_date").toLocalDateTime() : null
        );
    }
}
