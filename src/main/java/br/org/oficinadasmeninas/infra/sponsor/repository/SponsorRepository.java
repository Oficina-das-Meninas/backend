package br.org.oficinadasmeninas.infra.sponsor.repository;

import br.org.oficinadasmeninas.domain.sponsor.Sponsor;
import br.org.oficinadasmeninas.domain.sponsor.repository.ISponsorRepository;
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
public class SponsorRepository implements ISponsorRepository {
    private final JdbcTemplate jdbc;

    public SponsorRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    @Override
    public Sponsor insert(Sponsor sponsor) {
        UUID id = UUID.randomUUID();
        sponsor.setId(id);

        jdbc.update(
                SponsorQueryBuilder.INSERT_SPONSOR,
                id,
                sponsor.getMonthlyAmount(),
                sponsor.getBillingDay(),
                sponsor.getUserId(),
                sponsor.getSponsorSince(),
                sponsor.getSponsorUntil(),
                sponsor.getIsActive(),
                sponsor.getSubscriptionId()
        );

        return sponsor;
    }

    @Override
    public Sponsor update(Sponsor sponsor) {
        jdbc.update(
                SponsorQueryBuilder.UPDATE_SPONSOR,
                sponsor.getSponsorUntil(),
                sponsor.getIsActive(),
                sponsor.getSubscriptionId(),
                sponsor.getId()
        );

        return sponsor;
    }

    @Override
    public void activateByUserId(UUID userId) {
        jdbc.update(SponsorQueryBuilder.ACTIVATE_SPONSOR_BY_USER_ID, true, userId);
    }

    @Override
    public void suspendById(UUID id) {
        jdbc.update(
                SponsorQueryBuilder.SUSPEND_SPONSOR_BY_ID,
                LocalDateTime.now(),
                false,
                id
        );
    }

    @Override
    public List<Sponsor> findAll() {
        return List.of();
    }


    @Override
    public Optional<Sponsor> findById(UUID id) {

        try {
            var sponsor = jdbc.queryForObject(
                    SponsorQueryBuilder.GET_SPONSOR_BY_ID,
                    this::mapRowSponsor,
                    id
            );

            return Optional.ofNullable(sponsor);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Sponsor> findByUserId(UUID id) {

        return jdbc.query(
                SponsorQueryBuilder.GET_SPONSOR_BY_USER_ID,
                this::mapRowSponsor,
                id
        );
    }

    @Override
    public Optional<Sponsor> findActiveByUserId(UUID userId) {
        try {
            var sponsor = jdbc.queryForObject(
                    SponsorQueryBuilder.GET_ACTIVE_SPONSOR_BY_USER_ID,
                    this::mapRowSponsor,
                    userId
            );

            return Optional.ofNullable(sponsor);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Sponsor> findBySubscriptionId(UUID subscriptionId) {
        try {
            var sponsor = jdbc.queryForObject(
                    SponsorQueryBuilder.GET_SPONSOR_BY_SUBSCRIPTION_ID,
                    this::mapRowSponsor,
                    subscriptionId.toString()
            );

            return Optional.ofNullable(sponsor);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Sponsor mapRowSponsor(ResultSet rs, int rowNum) throws SQLException {
        return new Sponsor(
                UUID.fromString(rs.getString("id")),
                rs.getDouble("monthlyAmount"),
                rs.getInt("billingDay"),
                UUID.fromString(rs.getString("userId")),
                rs.getTimestamp("sponsorSince") != null ? rs.getTimestamp("sponsorSince").toLocalDateTime() : null,
                rs.getTimestamp("sponsorUntil") != null ? rs.getTimestamp("sponsorUntil").toLocalDateTime() : null,
                rs.getBoolean("isActive"),
                rs.getString("subscriptionId")
        );
    }
}
