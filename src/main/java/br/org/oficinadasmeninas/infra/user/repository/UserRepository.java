package br.org.oficinadasmeninas.infra.user.repository;

import br.org.oficinadasmeninas.domain.user.User;
import br.org.oficinadasmeninas.domain.user.repository.IUserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository implements IUserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public User insert(User user) {
        var id = UUID.randomUUID();
        user.setId(id);

        jdbc.update(
                UserQueryBuilder.INSERT_USER,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.getDocument()
        );

        return user;
    }

    @Override
    public User update(User user) {

        jdbc.update(
                UserQueryBuilder.UPDATE_USER,
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.getDocument(),
                user.getId()
        );

        return user;
    }

    @Override
    public List<User> findAll() {
        return jdbc.query(
                UserQueryBuilder.FIND_ALL_USERS,
                this::mapRowUser
        );
    }

    @Override
    public Optional<User> findById(UUID id) {
        try {
            var user = jdbc.queryForObject(
                    UserQueryBuilder.FIND_USER_BY_ID,
                    this::mapRowUser,
                    id
            );

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            var user = jdbc.queryForObject(
                    UserQueryBuilder.FIND_USER_BY_EMAIL,
                    this::mapRowUser,
                    email
            );

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByDocument(String email) {
        try {
            var user = jdbc.queryForObject(
                    UserQueryBuilder.FIND_USER_BY_DOCUMENT,
                    this::mapRowUser,
                    email
            );

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        var count = jdbc.queryForObject(
                UserQueryBuilder.EXISTS_USER_BY_EMAIL,
                Integer.class,
                email
        );

        return count != null && count > 0;
    }

    @Override
    public boolean existsByDocument(String document) {

        var count = jdbc.queryForObject(
                UserQueryBuilder.EXISTS_USER_BY_DOCUMENT,
                Integer.class,
                document
        );

        return count != null && count > 0;
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