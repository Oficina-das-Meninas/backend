package br.org.oficinadasmeninas.infra.partner.repository;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.partner.repository.IPartnerRepository;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.jdbc.core.JdbcTemplate;
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
    public PageDTO<Partner> findAll(int page, int pageSize) {
        String rowCountSql = "select count(*) from partners";
        long total = jdbc.queryForObject(rowCountSql, Integer.class);
        int totalPages = Math.toIntExact((total / pageSize) + (total % pageSize == 0 ? 0 : 1));

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
                pageSize,
                page
        );

        return new PageDTO<>(partners, total, totalPages);
    }

    private Partner mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Partner(
                rs.getObject("id", java.util.UUID.class),
                rs.getString("preview_url"),
                rs.getString("name")
        );
    }
}
