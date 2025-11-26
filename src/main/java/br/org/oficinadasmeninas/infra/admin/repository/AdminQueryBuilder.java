package br.org.oficinadasmeninas.infra.admin.repository;

public class AdminQueryBuilder {

    public static final String FIND_ADMIN_BY_ID = "SELECT id, name, email, password FROM admin WHERE id = ?";

    public static final String FIND_ADMIN_BY_EMAIL = "SELECT id, name, email, password FROM admin WHERE email = ?";

    public static final String INSERT_ADMIN = "INSERT INTO admin (id, name, email, password) VALUES (?, ?, ?, ?)";

    public static final String UPDATE_ADMIN = "UPDATE admin SET name = ?, email = ?, password = ? WHERE id = ?";
    
    public static final String UPDATE_PASSWORD = "UPDATE admin SET password = ? WHERE id = ?";
  
    public static final String SELECT_COUNT = """
    		SELECT count(*) FROM admin
    		WHERE (
    			unaccent(name) ILIKE unaccent(COALESCE('%' || ? || '%', name))
    			OR unaccent(email) ILIKE unaccent(COALESCE('%' || ? || '%', email)) 
    		)
    		""";
    
    public static final String GET_FILTERED_ADMINS = """
    		SELECT id, name, email, password FROM admin
    		WHERE (
    			unaccent(name) ILIKE unaccent(COALESCE('%' || ? || '%', name))
    			OR unaccent(email) ILIKE unaccent(COALESCE('%' || ? || '%', email)) 
    		)
    		""";
}
