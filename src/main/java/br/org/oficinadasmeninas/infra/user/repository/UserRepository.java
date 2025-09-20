package br.org.oficinadasmeninas.infra.user.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.org.oficinadasmeninas.domain.user.User;
import br.org.oficinadasmeninas.domain.user.repository.IUserRepository;

@Repository
public class UserRepository implements IUserRepository {

	private final JdbcTemplate jdbc;

	public UserRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	@Override
	public List<User> findAllUsers() {
		String sql = "SELECT id, name, email, password, phone, document FROM users";

		return jdbc.query(sql, this::mapRowUser);
	}

	@Override
	public Optional<User> findUserById(UUID id) {
		String sql = "SELECT id, name, email, password, phone, document FROM users WHERE id = ?";

		try {
            var user = jdbc.queryForObject(sql, this::mapRowUser, id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
	}

	@Override
	public Optional<User> findUserByEmail(String email) {
		String sql = "SELECT id, name, email, password, phone, document FROM users WHERE email = ?";

		try {
            var user = jdbc.queryForObject(sql, this::mapRowUser, email);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
	}

	@Override
	public UUID createUser(User user) {
		UUID id = UUID.randomUUID();
		String sql = "INSERT INTO users (id, name, email, password, phone, document) VALUES (?, ?, ?, ?, ?, ?)";
		jdbc.update(sql, id, user.getName(), user.getEmail(), user.getPassword(), user.getPhone(), user.getDocument());
		return id;
	}

	@Override
	public void updateUser(User user) {
		String sql = "UPDATE users SET name = ?, email = ?, password = ?, phone = ?, document = ? WHERE id = ?";
		jdbc.update(sql, user.getName(), user.getEmail(), user.getPassword(), user.getPhone(), user.getDocument(), user.getId());
	}

	private User mapRowUser(ResultSet rs, int rowNum) throws SQLException {
        var user = new User();
        user.setId(UUID.fromString(rs.getString("id")));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setDocument(rs.getString("document"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        return user;
    }

}
