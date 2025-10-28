package br.org.oficinadasmeninas.infra.user.repository;

public class UserQueryBuilder {

    public static final String FIND_ALL_USERS = "SELECT id, name, email, password, phone, document FROM users";

    public static final String FIND_USER_BY_ID = "SELECT id, name, email, password, phone, document FROM users WHERE id = ?";

    public static final String FIND_USER_BY_EMAIL = "SELECT id, name, email, password, phone, document FROM users WHERE email = ?";

    public static final String INSERT_USER = "INSERT INTO users (id, name, email, password, phone, document) VALUES (?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_USER = "UPDATE users SET name = ?, email = ?, password = ?, phone = ?, document = ? WHERE id = ?";

    public static final String EXISTS_USER_BY_EMAIL = "SELECT COUNT(*) FROM users WHERE email = ?";

    public static final String EXISTS_USER_BY_DOCUMENT = "SELECT COUNT(*) FROM users WHERE document = ?";

    public static final String FIND_USER_BY_DOCUMENT = "SELECT id, name, email, password, phone, document FROM users WHERE document = ?";
}
