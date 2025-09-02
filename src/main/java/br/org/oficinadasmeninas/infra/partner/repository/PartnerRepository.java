package br.org.oficinadasmeninas.infra.partner.repository;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.partner.repository.IPartnerRepository;
import br.org.oficinadasmeninas.domain.shared.SearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PartnerRepository implements IPartnerRepository {
    private final JdbcTemplate jdbc;

    public PartnerRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Page<Partner> findAll(SearchDTO partnerDTO) {
        Pageable pageable = PageRequest.of(
                Math.max(partnerDTO.getPageOrDefault() - 1, 0),
                partnerDTO.getPageSizeOrDefault()
        );

        String rowCountSql = "select count(*) from partners";
        int total = jdbc.queryForObject(rowCountSql, Integer.class);

        String querySql = """
	        select id
	              ,preview_url
	              ,name
	        from partners
	        order BY id
	       limit ? offset ?
	    """;

        List<Partner> partners = jdbc.query(
                querySql,
                this::mapRow,
                pageable.getPageSize(),
                pageable.getOffset()
        );

        return new PageImpl<Partner>(partners, pageable, total);
    }

    private Partner mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Partner(
                rs.getObject("id", java.util.UUID.class),
                rs.getString("preview_url"),
                rs.getString("name")
        );
    }
}
