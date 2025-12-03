package br.org.oficinadasmeninas.infra.user.repository;

public class UserQueryBuilder {

	public static final String FIND_ALL_USERS = """
	        SELECT 
	            u.id,
	            u.account_id,
	            u.phone,
	            u.document,
	            u.is_active,
	            acc.name,
	            acc.email,
	            acc.password
	        FROM users u
	        LEFT JOIN account acc ON acc.id = u.account_id
	        """;

	public static final String FIND_USER_BY_ID = """
	        SELECT 
	            u.id,
	            u.account_id,
	            u.phone,
	            u.document,
	            u.is_active,
	            acc.name,
	            acc.email,
	            acc.password
	        FROM users u
	        LEFT JOIN account acc ON acc.id = u.account_id
	        WHERE u.id = ?
	        """;

	public static final String FIND_USER_BY_EMAIL = """
	        SELECT 
	            u.id,
	            u.account_id,
	            u.phone,
	            u.document,
	            u.is_active,
	            acc.name,
	            acc.email,
	            acc.password
	        FROM users u
	        LEFT JOIN account acc ON acc.id = u.account_id
	        WHERE acc.email = ?
	        """;

	public static final String FIND_USER_BY_DOCUMENT = """
	        SELECT 
	            u.id,
	            u.account_id,
	            u.phone,
	            u.document,
	            u.is_active,
	            acc.name,
	            acc.email,
	            acc.password
	        FROM users u
	        LEFT JOIN account acc ON acc.id = u.account_id
	        WHERE u.document = ?
	        """;

	public static final String UPDATE_ACCOUNT = """
	        UPDATE account 
	        SET name = ?, email = ?, password = ?
	        WHERE id = ?
	        """;

	public static final String UPDATE_USER = """
	        UPDATE users 
	        SET phone = ?, document = ?, is_active = ?
	        WHERE id = ?
	        """;

	public static final String UPDATE_IS_ACTIVE = """
	        UPDATE users 
	        SET is_active = ?
	        WHERE id = ?
	        """;

	public static final String UPDATE_PASSWORD = """
	        UPDATE account SET password = ? WHERE id = ?
	        """;

	public static final String EXISTS_USER_BY_EMAIL = """
	        SELECT COUNT(*) 
	        FROM users u
	        LEFT JOIN account acc ON acc.id = u.account_id
	        WHERE acc.email = ?
	        """;

	public static final String EXISTS_USER_BY_DOCUMENT = """
	        SELECT COUNT(*) FROM users WHERE document = ?
	        """;

	public static final String INSERT_ACCOUNT = """
	        INSERT INTO account (id, name, email, password) 
	        VALUES (?, ?, ?, ?)
	        """;

	public static final String INSERT_USER = """
	        INSERT INTO users (id, phone, document, is_active, account_id) 
	        VALUES (?, ?, ?, ?, ?)
	        """;

	public static final String MARK_USER_AS_VERIFIED = """
	        UPDATE users SET is_active = true WHERE id = ?
	        """;

	public static final String EXISTS_USER_BY_DOCUMENT_AND_ACTIVE = """
	        SELECT COUNT(*) 
	        FROM users u
	        WHERE u.document = ? AND u.is_active = true
	        """;

	public static final String FIND_USER_BY_DOCUMENT_AND_INACTIVE = """
	        SELECT 
	            u.id,
	            u.account_id,
	            u.phone,
	            u.document,
	            u.is_active,
	            acc.name,
	            acc.email,
	            acc.password
	        FROM users u
	        LEFT JOIN account acc ON acc.id = u.account_id
	        WHERE u.document = ? AND u.is_active = false
	        """;

	public static final String DELETE_USER = """
	        DELETE FROM users WHERE id = ?
	        """;

	public static final String DELETE_ACCOUNT_BY_ID = """
	        DELETE FROM account WHERE id = ?
	        """;

	public static final String EXISTS_USER_BY_EMAIL_AND_ACTIVE = """
	        SELECT COUNT(*) 
	        FROM users u
	        LEFT JOIN account acc ON acc.id = u.account_id
	        WHERE acc.email = ? AND u.is_active = true
	        """;

	public static final String FIND_USER_BY_EMAIL_AND_INACTIVE = """
	        SELECT 
	            u.id,
	            u.account_id,
	            u.phone,
	            u.document,
	            u.is_active,
	            acc.name,
	            acc.email,
	            acc.password
	        FROM users u
	        LEFT JOIN account acc ON acc.id = u.account_id
	        WHERE acc.email = ? AND u.is_active = false
	        """;

}
