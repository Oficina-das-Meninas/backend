package br.org.oficinadasmeninas.infra.admin.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.org.oficinadasmeninas.domain.admin.Admin;
import br.org.oficinadasmeninas.domain.admin.repository.IAdminRepository;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

@Repository
public class AdminRepository implements IAdminRepository {

    private final JdbcTemplate jdbc;

    public AdminRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Admin insert(Admin admin) {

        var id = UUID.randomUUID();
        admin.setId(id);

        jdbc.update(
                AdminQueryBuilder.INSERT_ADMIN,
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPassword()
        );

        return admin;
    }

    @Override
    public Admin update(Admin admin) {

        jdbc.update(
                AdminQueryBuilder.UPDATE_ADMIN,
                admin.getName(),
                admin.getEmail(),
                admin.getPassword(),
                admin.getId()
        );

        return admin;
    }

    @Override
    public PageDTO<Admin> findByFilter(String searchTerm, int page, int pageSize){
    	var admins = jdbc.query(
                AdminQueryBuilder.GET_FILTERED_ADMINS,
                this::mapRowAdmin,
                searchTerm,
                searchTerm
        );
    	
    	var total = jdbc.queryForObject(
    			AdminQueryBuilder.SELECT_COUNT,
                Integer.class,
                searchTerm,
                searchTerm
        );
    	
        if (total == null) total = 0;

        int totalPages = Math.toIntExact((total / pageSize) + (total % pageSize == 0 ? 0 : 1));

        return new PageDTO<>(admins, total, totalPages);
    }

    @Override
    public Optional<Admin> findById(UUID id) {
        try {
            var admin = jdbc.queryForObject(
                    AdminQueryBuilder.FIND_ADMIN_BY_ID,
                    this::mapRowAdmin,
                    id
            );

            return Optional.ofNullable(admin);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        try {
            var admin = jdbc.queryForObject(
                    AdminQueryBuilder.FIND_ADMIN_BY_EMAIL,
                    this::mapRowAdmin,
                    email
            );

            return Optional.ofNullable(admin);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Admin mapRowAdmin(ResultSet rs, int rowNum) throws SQLException {
        var admin = new Admin();
        admin.setId(UUID.fromString(rs.getString("id")));
        admin.setName(rs.getString("name"));
        admin.setEmail(rs.getString("email"));
        admin.setPassword(rs.getString("password"));
        return admin;
    }
}
