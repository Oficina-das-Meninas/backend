package br.org.oficinadasmeninas.infra.admin.repository;

import br.org.oficinadasmeninas.domain.admin.Admin;
import br.org.oficinadasmeninas.domain.admin.repository.IAdminRepository;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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

        var accountId = UUID.randomUUID();

        jdbc.update(
                AdminQueryBuilder.INSERT_ACCOUNT,
                accountId,
                admin.getName(),
                admin.getEmail(),
                admin.getPassword()
        );

        var adminId = UUID.randomUUID();
        admin.setId(adminId);

        jdbc.update(
                AdminQueryBuilder.INSERT_ADMIN,
                adminId,
                accountId
        );

        return admin;
    }

    @Override
    public Admin update(Admin admin) {

        jdbc.update(
                AdminQueryBuilder.UPDATE_ACCOUNT,
                admin.getName(),
                admin.getEmail(),
                admin.getPassword(),
                admin.getAccountId()
        );

        return admin;
    }

    @Override
    public void deleteById(UUID id) {
        jdbc.update(AdminQueryBuilder.DELETE_ADMIN, id);
    }

    @Override
    public boolean existsById(UUID id) {

        return Boolean.TRUE.equals(
                jdbc.queryForObject(
                        AdminQueryBuilder.EXISTS_ADMIN_BY_ID,
                        Boolean.class,
                        id
                )
        );
    }

    @Override
    public PageDTO<Admin> findByFilter(String searchTerm, int page, int pageSize) {
        var admins = jdbc.query(
                AdminQueryBuilder.GET_FILTERED_ADMINS,
                this::mapRowAdmin,
                searchTerm,
                searchTerm,
                pageSize,
                page * pageSize
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

        return jdbc.query(
                AdminQueryBuilder.FIND_ADMIN_BY_ID,
                this::mapRowAdmin,
                id
        ).stream().findFirst();
    }

    @Override
    public Optional<Admin> findByEmail(String email) {

        return jdbc.query(
                AdminQueryBuilder.FIND_ADMIN_BY_EMAIL,
                this::mapRowAdmin,
                email
        ).stream().findFirst();
    }

    @Override
    public void updatePassword(UUID id, String encodedPassword) {
        jdbc.update(
                AdminQueryBuilder.UPDATE_PASSWORD,
                encodedPassword,
                id
        );
    }

    private Admin mapRowAdmin(ResultSet rs, int rowNum) throws SQLException {
        var admin = new Admin();
        admin.setId(UUID.fromString(rs.getString("id")));
        admin.setName(rs.getString("name"));
        admin.setEmail(rs.getString("email"));
        admin.setPassword(rs.getString("password"));
        admin.setAccountId(UUID.fromString(rs.getString("account_id")));
        return admin;
    }
}
