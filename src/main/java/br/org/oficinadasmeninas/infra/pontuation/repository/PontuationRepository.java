package br.org.oficinadasmeninas.infra.pontuation.repository;

import br.org.oficinadasmeninas.domain.pontuation.Pontuation;
import br.org.oficinadasmeninas.domain.pontuation.dto.GetUserPontuationsDto;
import br.org.oficinadasmeninas.domain.pontuation.repository.IPontuationRepository;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class PontuationRepository implements IPontuationRepository {

    private final JdbcTemplate jdbc;

    public PontuationRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public PageDTO<Pontuation> findByIdAndFilters(UUID id, GetUserPontuationsDto getUserPontuationsDto) {
        var pontuations = jdbc.query(
                PontuationQueryBuilder.GET_FILTERED_PONTUATIONS,
                this::mapRow,
                id,
                getUserPontuationsDto.donationMethod(),
                getUserPontuationsDto.startDate(),
                getUserPontuationsDto.endDate(),
                getUserPontuationsDto.pageSize(),
                getUserPontuationsDto.pageSize() * getUserPontuationsDto.page()
        );

        var total = jdbc.queryForObject(
                PontuationQueryBuilder.SELECT_COUNT,
                Integer.class,
                id,
                getUserPontuationsDto.donationMethod(),
                getUserPontuationsDto.startDate(),
                getUserPontuationsDto.endDate());

        if (total == null) total = 0;

        int totalPages = Math.toIntExact((total / getUserPontuationsDto.pageSize()) + (total % getUserPontuationsDto.pageSize() == 0 ? 0 : 1));

        return new PageDTO<>(pontuations, total, totalPages);
    }

    private Pontuation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Pontuation(
                rs.getObject("id", java.util.UUID.class),
                rs.getObject("user_id", java.util.UUID.class),
                rs.getObject("payment_id", java.util.UUID.class),
                rs.getLong("donated_value"),
                rs.getObject("donated_date", LocalDateTime.class),
                rs.getLong("earned_points"),
                rs.getLong("total_points"),
                rs.getString("method"),
                rs.getInt("recurrence_sequence"),
                rs.getBoolean("is_first_donation"));
    }
}