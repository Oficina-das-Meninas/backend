package br.org.oficinadasmeninas.infra.donation.repository;

import br.org.oficinadasmeninas.domain.donation.Donation;
import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.donation.repository.IDonationRepository;
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
public class DonationRepository implements IDonationRepository {

    private final JdbcTemplate jdbc;

    public DonationRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Donation insert(Donation donation) {

        var id = donation.getId() != null ? donation.getId() : UUID.randomUUID();
        donation.setId(id);

        jdbc.update(
                DonationQueryBuilder.INSERT_DONATION,
                id,
                donation.getValue(),
                donation.getDonationAt(),
                donation.getUserId(),
                donation.getStatus().name()
        );

        return donation;
    }

    @Override
    public Donation updateStatus(Donation donation) {

        jdbc.update(
                DonationQueryBuilder.UPDATE_DONATION_STATUS,
                donation.getStatus().name(),
                donation.getId()
        );

        return donation;
    }

    @Override
    public List<Donation> findAll() {
        return jdbc.query(DonationQueryBuilder.SELECT_ALL_DONATIONS, this::mapRowDonation);
    }

    @Override
    public Optional<Donation> findById(UUID id) {
        try {
            var donation = jdbc.queryForObject(
                    DonationQueryBuilder.SELECT_DONATION_BY_ID,
                    this::mapRowDonation,
                    id
            );

            return Optional.ofNullable(donation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Donation mapRowDonation(ResultSet rs, int rowNum) throws SQLException {
        var donation = new Donation();
        donation.setId(rs.getObject("id", UUID.class));
        donation.setValue(rs.getLong("value"));
        donation.setDonationAt(rs.getObject("donation_at", LocalDateTime.class));
        donation.setUserId(rs.getObject("user_id", UUID.class));
        donation.setStatus(DonationStatusEnum.valueOf(rs.getString("status")));
        return donation;
    }
}
