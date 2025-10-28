package br.org.oficinadasmeninas.infra.donation.repository;

import br.org.oficinadasmeninas.domain.donation.Donation;
import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.donation.dto.DonationWithDonorDto;
import br.org.oficinadasmeninas.domain.donation.dto.GetDonationDto;
import br.org.oficinadasmeninas.domain.donation.repository.IDonationRepository;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
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
	public List<Donation> findAll() {
		return jdbc.query(DonationQueryBuilder.SELECT_ALL_DONATIONS, this::mapRowDonation);
	}

    public PageDTO<DonationWithDonorDto> getFiltered(GetDonationDto getDonationDto) {
        String donationStatus = getDonationDto.status() != null ? getDonationDto.status().name() : null;
        String donationType = getDonationDto.donationType();
        String donorName = getDonationDto.donorName();

        List<DonationWithDonorDto> donations = jdbc.query(
                DonationQueryBuilder.GET_FILTERED_DONATIONS,
                this::mapRowDonationWithDonor,
                donorName, donorName,
                getDonationDto.startDate(), getDonationDto.startDate(),
                getDonationDto.endDate(), getDonationDto.endDate(),
                donationStatus, donationStatus,
                donationType, donationType,
                getDonationDto.pageSize(),
                getDonationDto.page() * getDonationDto.pageSize()
        );

        int total = jdbc.queryForObject(
                DonationQueryBuilder.SELECT_COUNT,
                Integer.class,
                donorName, donorName,
                getDonationDto.startDate(), getDonationDto.startDate(),
                getDonationDto.endDate(), getDonationDto.endDate(),
                donationStatus, donationStatus,
                donationType, donationType
        );

        int totalPages = Math.toIntExact((total / getDonationDto.pageSize()) + (total % getDonationDto.pageSize() == 0 ? 0 : 1));

        return new PageDTO<>(donations, total, totalPages);
    }

    @Override
	public Optional<Donation> findById(UUID id) {
		try {
			Donation donation = jdbc.queryForObject(DonationQueryBuilder.SELECT_DONATION_BY_ID, this::mapRowDonation, id);
			return Optional.ofNullable(donation);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public UUID create(Donation donation) {
		UUID id = donation.getId() != null ? donation.getId() : UUID.randomUUID();
		jdbc.update(DonationQueryBuilder.INSERT_DONATION, id, donation.getValue(), donation.getDonationAt(),
				donation.getUserId(), donation.getStatus().name());
		return id;
	}

	@Override
	public void updateStatus(UUID id, DonationStatusEnum status) {
		jdbc.update(DonationQueryBuilder.UPDATE_DONATION_STATUS, status.name(), id);
	}

	private Donation mapRowDonation(ResultSet rs, int rowNum) throws SQLException {
		Donation donation = new Donation();
		donation.setId(rs.getObject("id", UUID.class));
		donation.setValue(rs.getLong("value"));
		donation.setDonationAt(rs.getObject("donation_at", LocalDateTime.class));
		donation.setUserId(rs.getObject("user_id", UUID.class));
		donation.setStatus(DonationStatusEnum.valueOf(rs.getString("status")));
		return donation;
	}

	private DonationWithDonorDto mapRowDonationWithDonor(ResultSet rs, int rowNum) throws SQLException {
        String userIdString = rs.getString("user_id");
		return new DonationWithDonorDto(
				UUID.fromString(rs.getString("id")),
				rs.getBigDecimal("value"),
				rs.getTimestamp("donation_at").toLocalDateTime(),
                userIdString != null ? UUID.fromString(userIdString) : null,
                rs.getString("donation_type"),
                DonationStatusEnum.valueOf(rs.getString("status").toUpperCase()),
                rs.getString("sponsor_status"),
				rs.getString("donor_name")
		);
	}

}
