package br.org.oficinadasmeninas.infra.sponsor.repository;

import br.org.oficinadasmeninas.domain.sponsor.Sponsor;
import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.dto.UpdateSponsorDto;
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
    public List<Sponsor> findAllSponsors() {
        return List.of();
    }

    @Override
    public Optional<Sponsor> findById(UUID id) {
        String sql = "SELECT id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId FROM sponsors WHERE id = ?";

        try {
            var sponsor = jdbc.queryForObject(sql, this::mapRowSponsor);
            return Optional.ofNullable(sponsor);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Sponsor> findBySubscriptionId(UUID subscriptionId) {
        String sql = "SELECT id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId FROM sponsors WHERE subscriptionId = ?";

        try {
            var sponsor = jdbc.queryForObject(sql, this::mapRowSponsor, subscriptionId.toString());
            return Optional.ofNullable(sponsor);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Sponsor> findByUserId(UUID id) {
        String sql = """
        SELECT id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId
        FROM sponsors
        WHERE userId = ?
        """;

        return jdbc.query(sql, this::mapRowSponsor, id);
    }

    @Override
    public Optional<Sponsor> findActiveByUserId(UUID id) {
        String sql = "SELECT id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId FROM sponsors WHERE userId = ? AND isActive = true";

        try {
            var sponsor = jdbc.queryForObject(sql, this::mapRowSponsor, id);
            return Optional.ofNullable(sponsor);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public UUID createSponsor(Sponsor sponsor) {
        UUID id = UUID.randomUUID();
        String sql = "INSERT INTO sponsors (id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbc.update(sql,
                id,
                sponsor.getMonthlyAmount(),
                sponsor.getBillingDay(),
                sponsor.getUserId(),
                sponsor.getSponsorSince(),
                sponsor.getSponsorUntil(),
                sponsor.getIsActive(),
                sponsor.getSubscriptionId()
        );
        return id;
    }

    @Override
    public void updateSponsor(UpdateSponsorDto sponsor) {
        String sql = "UPDATE sponsors SET sponsorUntil = ?, isActive = ?, subscriptionId = ? " +
                "WHERE id = ?";

        jdbc.update(sql,
           sponsor.sponsorUntil(),
           sponsor.isActive(),
           sponsor.subscriptionId(),
           sponsor.id()
        );
    }

    @Override
    public void suspendSponsor(UUID id) {
        String sql = "UPDATE sponsors SET sponsorUntil = ?, isActive = ? " +
                "WHERE id = ?";

        jdbc.update(sql,
          LocalDateTime.now(),
          false,
          id
        );
    }

    @Override
    public void activeSponsorByuserId(UUID userId) {
        String sql = "UPDATE sponsors SET isActive = ? " +
                "WHERE userId = ?";

        jdbc.update(sql,
                true,
                userId
        );
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
