package br.org.oficinadasmeninas.infra.admin.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.org.oficinadasmeninas.domain.admin.Admin;
import br.org.oficinadasmeninas.domain.admin.repository.IAdminRepository;

@Repository
public class AdminRepository implements IAdminRepository {

	private final JdbcTemplate jdbc;

	public AdminRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public List<Admin> findAllAdmins() {
		return jdbc.query(AdminQueryBuilder.FIND_ALL_ADMINS, this::mapRowAdmin);
	}

	@Override
	public Optional<Admin> findAdminById(UUID id) {
		try {
            var admin = jdbc.queryForObject(AdminQueryBuilder.FIND_ADMIN_BY_ID, this::mapRowAdmin, id);
            return Optional.ofNullable(admin);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
	}

	@Override
	public Optional<Admin> findAdminByEmail(String email) {
        try {
            var admin = jdbc.queryForObject(AdminQueryBuilder.FIND_ADMIN_BY_EMAIL, this::mapRowAdmin, email);
            return Optional.ofNullable(admin);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
	}

	@Override
	public UUID createAdmin(Admin admin) {
		UUID id = UUID.randomUUID();
		jdbc.update(AdminQueryBuilder.INSERT_ADMIN, id, admin.getName(), admin.getEmail(), admin.getPassword());
		return id;
	}

	@Override
	public void updateAdmin(Admin admin) {
		jdbc.update(AdminQueryBuilder.UPDATE_ADMIN, admin.getName(), admin.getEmail(), admin.getPassword(), admin.getId());
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
