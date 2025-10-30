package br.org.oficinadasmeninas.infra.partner.repository;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.partner.repository.IPartnerRepository;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PartnerRepository implements IPartnerRepository {
    private final JdbcTemplate jdbc;

    public PartnerRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public PageDTO<Partner> findByFilter(String searchTerm, int page, int pageSize) {
        List<Partner> partners = jdbc.query(
                PartnerQueryBuilder.GET_PARTNERS,
                this::mapRow,
                searchTerm,
                pageSize,
                page * pageSize
        );

        var total = jdbc.queryForObject(
                PartnerQueryBuilder.SELECT_COUNT,
                Integer.class,
                searchTerm
        );

        if (total == null) total = 0;

        int totalPages = Math.toIntExact((total / pageSize) + (total % pageSize == 0 ? 0 : 1));

        return new PageDTO<>(partners, total, totalPages);
    }

    @Override
    public Optional<Partner> findById(UUID id) {
        try {
            var partner = jdbc.queryForObject(PartnerQueryBuilder.GET_PARTNER_BY_ID, this::mapRow, id);
            return Optional.ofNullable(partner);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Partner insert(Partner partner) {
        var id = UUID.randomUUID();
        partner.setId(id);

        jdbc.update(PartnerQueryBuilder.CREATE_PARTNER,
                partner.getId(),
                partner.getPreviewImageUrl(),
                partner.getName()
        );

        return partner;
    }

    @Override
    public Partner update(Partner partner) {

        jdbc.update(PartnerQueryBuilder.UPDATE_PARTNER,
                partner.getPreviewImageUrl(),
                partner.getName(),
                partner.getId()
        );

        return partner;
    }


	@Override
	public void deleteById(UUID id) {
		jdbc.update(PartnerQueryBuilder.DELETE_PARTNER, id);
	}
    
    private Partner mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Partner(
                rs.getObject("id", java.util.UUID.class),
                rs.getString("preview_image_url"),
                rs.getString("name")
        );
    }
    
}