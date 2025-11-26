package br.org.oficinadasmeninas.infra.user.repository;

public class UserQueryBuilder {

    public static final String FIND_ALL_USERS = "SELECT id, name, email, password, phone, document, is_active FROM users";

    public static final String FIND_USER_BY_ID = "SELECT id, name, email, password, phone, document, is_active FROM users WHERE id = ?";

    public static final String FIND_USER_BY_EMAIL = "SELECT id, name, email, password, phone, document, is_active FROM users WHERE email = ?";

    public static final String INSERT_USER = "INSERT INTO users (id, name, email, password, phone, document, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_USER = "UPDATE users SET name = ?, email = ?, password = ?, phone = ?, document = ? WHERE id = ?";

    public static final String EXISTS_USER_BY_EMAIL = "SELECT COUNT(*) FROM users WHERE email = ?";

    public static final String EXISTS_USER_BY_DOCUMENT = "SELECT COUNT(*) FROM users WHERE document = ?";

    public static final String FIND_USER_BY_DOCUMENT = "SELECT id, name, email, password, phone, document, is_active FROM users WHERE document = ?";
    
    public static final String MARK_USER_AS_VERIFIED = "UPDATE users SET is_active = true WHERE id = ?";
    
    public static final String UPDATE_PASSWORD = "UPDATE users SET password = ? WHERE id = ?";
}
