package br.org.oficinadasmeninas.infra.donation.repository;

import br.org.oficinadasmeninas.domain.donation.Donation;
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

import static br.org.oficinadasmeninas.infra.donation.repository.DonationQueryBuilder.ALLOWED_SORT_FIELDS;

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
                donation.getCheckoutId(),
                donation.getGateway() != null ? donation.getGateway().name() : null,
                donation.getSponsorshipId(),
                donation.getMethod() != null ? donation.getMethod().name() : null,
                donation.getUserId(),
                donation.getDonationAt()
        );

        return donation;
    }


    @Override
    public Donation updateMethod(Donation donation) {

        jdbc.update(
                DonationQueryBuilder.UPDATE_DONATION_METHOD,
                donation.getMethod() != null ? donation.getMethod().name() : null,
                donation.getId()
        );

        return donation;
    }

    @Override
    public List<Donation> findAll() {
        return jdbc.query(DonationQueryBuilder.SELECT_ALL_DONATIONS, this::mapRowDonation);
    }

    private String buildOrderByClause(String sortField, String sortDirection) {
        String field = ALLOWED_SORT_FIELDS.getOrDefault(sortField, "d.donation_at");
        String direction = "desc".equalsIgnoreCase(sortDirection) ? "DESC" : "ASC";
        return field + " " + direction + ", d.id ASC";
    }

    public PageDTO<DonationWithDonorDto> findByFilter(GetDonationDto getDonationDto) {
        String donationType = getDonationDto.donationType();
        String donorName = getDonationDto.donorName();

        String orderBy = buildOrderByClause(
                getDonationDto.sortField(),
                getDonationDto.sortDirection()
        );

        String query = DonationQueryBuilder.GET_FILTERED_DONATIONS
                .replace("%ORDER_BY%", "ORDER BY " + orderBy);

        List<DonationWithDonorDto> donations = jdbc.query(
                query,
                this::mapRowDonationWithDonor,
                donorName, donorName,
                getDonationDto.startDate(), getDonationDto.startDate(),
                getDonationDto.endDate(), getDonationDto.endDate(),
                donationType, donationType,
                getDonationDto.pageSize(),
                getDonationDto.page() * getDonationDto.pageSize()
        );

        var total = jdbc.queryForObject(
                DonationQueryBuilder.SELECT_COUNT,
                Integer.class,
                donorName, donorName,
                getDonationDto.startDate(), getDonationDto.startDate(),
                getDonationDto.endDate(), getDonationDto.endDate(),
                donationType, donationType
        );

        if (total == null) total = 0;

        int totalPages = Math.toIntExact((total / getDonationDto.pageSize()) + (total % getDonationDto.pageSize() == 0 ? 0 : 1));

        return new PageDTO<>(donations, total, totalPages);
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

    @Override
    public List<Donation> findPendingCheckoutsByUserId(UUID id) {
        try{
            return jdbc.query(DonationQueryBuilder.SELECT_PENDING_CHECKOUTS_BY_USER_ID, this::mapRowDonation, id);
        }catch (EmptyResultDataAccessException e){
            return List.of();
        }
    }

	private Donation mapRowDonation(ResultSet rs, int rowNum) throws SQLException {
		Donation donation = new Donation();
		donation.setId(rs.getObject("id", UUID.class));
		donation.setValue(rs.getDouble("value"));
		donation.setCheckoutId(rs.getString("checkout_id"));

		String gateway = rs.getString("gateway");
		if (gateway != null) {
			donation.setGateway(br.org.oficinadasmeninas.domain.paymentgateway.PaymentGatewayEnum.valueOf(gateway));
		}

		donation.setSponsorshipId(rs.getObject("sponsorship_id", UUID.class));

		String method = rs.getString("method");
		if (method != null) {
			donation.setMethod(br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum.valueOf(method));
		}

		donation.setUserId(rs.getObject("user_id", UUID.class));
		donation.setDonationAt(rs.getObject("donation_at", LocalDateTime.class));
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
                rs.getString("sponsor_status"),
				rs.getString("donor_name")
		);
	}

}
