package br.org.oficinadasmeninas.infra.admin.repository;

import br.org.oficinadasmeninas.domain.admin.Admin;
import br.org.oficinadasmeninas.domain.admin.repository.IAdminRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public List<Admin> findAll() {
        return jdbc.query(
                AdminQueryBuilder.FIND_ALL_ADMINS,
                this::mapRowAdmin
        );
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
