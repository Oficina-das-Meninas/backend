package br.org.oficinadasmeninas.infra.admin.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
	public List<Admin> findAll() {
		String sql = "SELECT id, name, email, password FROM admin";

		return jdbc.query(sql, (rs, rowNum) -> {
			Admin admin = new Admin();
			admin.setId(UUID.fromString(rs.getString("id")));
			admin.setName(rs.getString("name"));
			admin.setEmail(rs.getString("email"));
			admin.setPassword(rs.getString("password"));
			return admin;
		});
	}

	@Override
	public Optional<Admin> findAdminById(UUID id) {
		String sql = "SELECT id, name, email, password FROM admin WHERE id = ?";

		return jdbc.query(sql, rs -> {
			if (rs.next()) {
				Admin admin = new Admin();
				admin.setId(UUID.fromString(rs.getString("id")));
				admin.setName(rs.getString("name"));
				admin.setEmail(rs.getString("email"));
				admin.setPassword(rs.getString("password"));
				return Optional.of(admin);
			} else {
				return Optional.empty();
			}
		}, id);
	}

	@Override
	public Optional<Admin> findAdminByEmail(String email) {
		String sql = "SELECT id, name, email, password FROM admin WHERE email = ?";

		return jdbc.query(sql, rs -> {
			if (rs.next()) {
				Admin admin = new Admin();
				admin.setId(UUID.fromString(rs.getString("id")));
				admin.setName(rs.getString("name"));
				admin.setEmail(rs.getString("email"));
				admin.setPassword(rs.getString("password"));
				return Optional.of(admin);
			} else {
				return Optional.empty();
			}
		}, email);
	}

	@Override
	public UUID createAdmin(Admin admin) {
		UUID id = UUID.randomUUID();
		String sql = "INSERT INTO admin (id, name, email, password) VALUES (?, ?, ?, ?)";
		jdbc.update(sql, id, admin.getName(), admin.getEmail(), admin.getPassword());
		return id;
	}

	@Override
	public void updateAdmin(Admin admin) {
		String sql = "UPDATE admin SET name = ?, email = ?, password = ? WHERE id = ?";
		jdbc.update(sql, admin.getName(), admin.getEmail(), admin.getPassword(), admin.getId());
	}

}
