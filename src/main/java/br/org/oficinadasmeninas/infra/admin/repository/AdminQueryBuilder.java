package br.org.oficinadasmeninas.infra.admin.repository;

public class AdminQueryBuilder {

    public static final String FIND_ADMIN_BY_ID = """
				SELECT
					adm.id, 
					adm.account_id,
					acc.name, 
					acc.email, 
					acc.password
				FROM admin adm
				LEFT JOIN account acc on acc.id = adm.account_id
				WHERE adm.id = ?
    		""";

    public static final String FIND_ADMIN_BY_EMAIL = """
			SELECT 
				adm.id, 
				adm.account_id,
				acc.name, 
				acc.email, 
				acc.password
			FROM admin adm
			LEFT JOIN account acc on acc.id = adm.account_id
			WHERE acc.email = ?
    	""";

    public static final String UPDATE_ACCOUNT = "UPDATE account SET name = ?, email = ?, password = ? WHERE id = ?";
    
    public static final String SELECT_COUNT = """
    		SELECT count(*) FROM admin
    		WHERE (
    			unaccent(name) ILIKE unaccent(COALESCE('%' || ? || '%', name))
    			OR unaccent(email) ILIKE unaccent(COALESCE('%' || ? || '%', email)) 
    		)
    		""";
    
    public static final String GET_FILTERED_ADMINS = """
    		SELECT 
				adm.id, 
				adm.account_id,
				acc.name, 
				acc.email, 
				acc.password
			FROM admin adm
			LEFT JOIN account acc on acc.id = adm.account_id
    		WHERE (
    			unaccent(acc.name) ILIKE unaccent(COALESCE('%' || ? || '%', acc.name))
    			OR unaccent(acc.email) ILIKE unaccent(COALESCE('%' || ? || '%', acc.email)) 
    		)
    		""";
    
    public static final String INSERT_ACCOUNT = "INSERT INTO account (id, name, email, password) VALUES (?, ?, ?, ?)";
    
    public static final String INSERT_ADMIN = "INSERT INTO admin (id, account_id) VALUES (?, ?)";
}
