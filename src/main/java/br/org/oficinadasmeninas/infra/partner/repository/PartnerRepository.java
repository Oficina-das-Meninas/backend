package br.org.oficinadasmeninas.infra.partner.repository;

import br.org.oficinadasmeninas.domain.partner.Partner;
import br.org.oficinadasmeninas.domain.partner.dto.CreatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.dto.UpdatePartnerDto;
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
    public PageDTO<Partner> findAll(int page, int pageSize) {
        List<Partner> partners = jdbc.query(
                PartnerQueryBuilder.GET_PARTNERS,
                this::mapRow,
                pageSize,
                page * pageSize
        );

        long total = jdbc.queryForObject(PartnerQueryBuilder.SELECT_COUNT, Integer.class);
        int totalPages = Math.toIntExact((total / pageSize) + (total % pageSize == 0 ? 0 : 1));

        return new PageDTO<>(partners, total, totalPages);
    }

    @Override
    public Optional<Partner> getById(UUID id) {
        try
        {
            var partner = jdbc.queryForObject(PartnerQueryBuilder.GET_PARTNER_BY_ID, this::mapRow, id);
            return Optional.of(partner);
        }
        catch (EmptyResultDataAccessException e)
        {
            return Optional.empty();
        }
    }

    public UUID create(CreatePartnerDto createPartnerDto, String previewFileName) {
        var id = UUID.randomUUID();

        jdbc.update(PartnerQueryBuilder.CREATE_PARTNER,
                id,
                previewFileName,
                createPartnerDto.name());

        return id;
    }

    @Override
    public void update(UpdatePartnerDto updatePartnerDto, String previewFileName) {
        jdbc.update(PartnerQueryBuilder.UPDATE_PARTNER,
                previewFileName,
                updatePartnerDto.name(),
                updatePartnerDto.isActive() == null || updatePartnerDto.isActive(),
                updatePartnerDto.id());
    }

    private Partner mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Partner(
                rs.getObject("id", java.util.UUID.class),
                rs.getString("preview_image_url"),
                rs.getString("name")
        );
    }
}