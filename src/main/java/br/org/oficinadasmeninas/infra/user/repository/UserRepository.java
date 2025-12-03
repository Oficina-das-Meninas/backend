package br.org.oficinadasmeninas.infra.user.repository;

import br.org.oficinadasmeninas.domain.user.User;
import br.org.oficinadasmeninas.domain.user.repository.IUserRepository;
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

        var accountId = UUID.randomUUID();
        jdbc.update(
                UserQueryBuilder.INSERT_ACCOUNT,
                accountId,
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );

        var userId = UUID.randomUUID();
        user.setId(userId);
        user.setAccountId(accountId);

        jdbc.update(
                UserQueryBuilder.INSERT_USER,
                user.getId(),
                user.getPhone(),
                user.getDocument(),
                user.isActive(),
                accountId
        );

        return user;
    }

    @Override
    public User update(User user) {

        jdbc.update(
                UserQueryBuilder.UPDATE_ACCOUNT,
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getAccountId()
        );

        jdbc.update(
                UserQueryBuilder.UPDATE_USER,
                user.getPhone(),
                user.getDocument(),
                user.isActive(),
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

        return jdbc.query(
                UserQueryBuilder.FIND_USER_BY_ID,
                this::mapRowUser,
                id
        ).stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return jdbc.query(
                UserQueryBuilder.FIND_USER_BY_EMAIL,
                this::mapRowUser,
                email
        ).stream().findFirst();
    }

    @Override
    public Optional<User> findByDocument(String document) {

        return jdbc.query(
                UserQueryBuilder.FIND_USER_BY_DOCUMENT,
                this::mapRowUser,
                document
        ).stream().findFirst();
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

    @Override
    public void markUserAsVerified(UUID id) {
        jdbc.update(
                UserQueryBuilder.MARK_USER_AS_VERIFIED,
                id
        );
    }

    @Override
    public void updatePassword(UUID accountId, String encodedPassword) {
        jdbc.update(
                UserQueryBuilder.UPDATE_PASSWORD,
                encodedPassword,
                accountId
        );
    }

    @Override
    public boolean existsByDocumentAndActive(String document) {
        var count = jdbc.queryForObject(
                UserQueryBuilder.EXISTS_USER_BY_DOCUMENT_AND_ACTIVE,
                Integer.class,
                document
        );

        return count != null && count > 0;
    }

    @Override
    public List<User> findByDocumentAndInactive(String document) {
        return jdbc.query(
                UserQueryBuilder.FIND_USER_BY_DOCUMENT_AND_INACTIVE,
                this::mapRowUser,
                document
        );
    }

    @Override
    public void delete(UUID id) {
        var user = findById(id);

        if (user.isPresent()) {
            jdbc.update(
                    UserQueryBuilder.DELETE_USER,
                    id
            );

            var accountId = user.get().getAccountId();
            if (accountId != null) {
                jdbc.update(
                        UserQueryBuilder.DELETE_ACCOUNT_BY_ID,
                        accountId
                );
            }
        }
    }

    @Override
    public boolean existsByEmailAndActive(String email) {
        var count = jdbc.queryForObject(
                UserQueryBuilder.EXISTS_USER_BY_EMAIL_AND_ACTIVE,
                Integer.class,
                email
        );

        return count != null && count > 0;
    }

    @Override
    public List<User> findByEmailAndInactive(String email) {
        return jdbc.query(
                UserQueryBuilder.FIND_USER_BY_EMAIL_AND_INACTIVE,
                this::mapRowUser,
                email
        );
    }

    private User mapRowUser(ResultSet rs, int rowNum) throws SQLException {
        var user = new User();
        user.setId(UUID.fromString(rs.getString("id")));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setDocument(rs.getString("document"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setAccountId(UUID.fromString(rs.getString("account_id")));
        user.setIsActive(rs.getBoolean("is_active"));
        return user;
    }

}