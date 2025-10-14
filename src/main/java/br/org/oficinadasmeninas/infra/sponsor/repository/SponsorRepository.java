package br.org.oficinadasmeninas.infra.sponsor.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.org.oficinadasmeninas.domain.sponsor.Sponsor;
import br.org.oficinadasmeninas.domain.sponsor.dto.UpdateSponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.repository.ISponsorRepository;

@Repository
public class SponsorRepository implements ISponsorRepository {
    private final JdbcTemplate jdbc;

    public SponsorRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    
    @Override
	public List<Sponsor> findAllSponsors() {
    	 String sql = "SELECT id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId FROM sponsors";

         return jdbc.query(sql, this::mapRowSponsor);
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
	public Optional<Sponsor> findByUserId(UUID id) {
		String sql = "SELECT id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId FROM sponsors WHERE userId = ?";

        try {
            var sponsor = jdbc.queryForObject(sql, this::mapRowSponsor, id.toString());
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

    private Sponsor mapRowSponsor(ResultSet rs, int rowNum) throws SQLException {
        return new Sponsor(
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

	@Override
	public void suspendSponsor(UUID id) {
		String sql = "UPDATE sponsors SET isActive = ? " +
                "WHERE id = ?";

        jdbc.update(sql, false, id);
	}
}
