package br.org.oficinadasmeninas.infra.donor.repository;

import br.org.oficinadasmeninas.domain.donor.dto.DonorDto;
import br.org.oficinadasmeninas.domain.donor.repository.IDonorRepository;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Repository
public class DonorRepository implements IDonorRepository {

    private final JdbcTemplate jdbc;

    public DonorRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public PageDTO<DonorDto> findAll(
            int page, int pageSize,
            String donorBadge,
            String sortField, String sortDirection,
            String searchTerm
    ) {
        String query = String.format(
                DonorQueryBuilder.GET_DONORS,
                sortDirection.equalsIgnoreCase("DESC") ? "DESC" : "ASC"
        );

        var donors = jdbc.query(
                query,
                this::mapRow,
                searchTerm, searchTerm, searchTerm,
                donorBadge, donorBadge,
                sortField, sortField, sortField, sortField, sortField,
                pageSize, page * pageSize
        );

        var total = jdbc.queryForObject(
                DonorQueryBuilder.SELECT_COUNT,
                Integer.class,
                searchTerm, searchTerm, searchTerm,
                donorBadge, donorBadge
        );

        if (total == null) total = 0;

        int totalPages = (int) Math.ceil((double) total / pageSize);

        return new PageDTO<>(donors, total, totalPages);
    }

    private DonorDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DonorDto(
                rs.getObject("id", UUID.class),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("badge"),
                rs.getInt("total_points"),
                rs.getDouble("total_donated_value")
        );
    }
}