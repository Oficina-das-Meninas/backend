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
import java.util.Optional;
import java.util.UUID;

@Repository
public class SponsorRepository implements ISponsorRepository {
    private final JdbcTemplate jdbc;

    public SponsorRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<SponsorDto> findById(UUID id) {
        String sql = "SELECT id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId FROM sponsors WHERE id = ?";

        try {
            var sponsor = jdbc.queryForObject(sql, this::mapRowSponsor);
            return Optional.ofNullable(sponsor);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<SponsorDto> findBySubscriptionId(UUID subscriptionId) {
        String sql = "SELECT id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId FROM sponsors WHERE subscriptionId = ?";

        try {
            var sponsor = jdbc.queryForObject(sql, this::mapRowSponsor, subscriptionId.toString());
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

    private SponsorDto mapRowSponsor(ResultSet rs, int rowNum) throws SQLException {
        return new SponsorDto(
                UUID.fromString(rs.getString("id")),
                rs.getLong("monthlyAmount"),
                rs.getInt("billingDay"),
                UUID.fromString(rs.getString("userId")),
                rs.getTimestamp("sponsorSince") != null ? rs.getTimestamp("sponsorSince").toLocalDateTime() : null,
                rs.getTimestamp("sponsorUntil") != null ? rs.getTimestamp("sponsorUntil").toLocalDateTime() : null,
                rs.getBoolean("isActive"),
                rs.getString("subscriptionId")
        );
    }
}
