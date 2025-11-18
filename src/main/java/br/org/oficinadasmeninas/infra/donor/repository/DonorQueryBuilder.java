package br.org.oficinadasmeninas.infra.donor.repository;

public class DonorQueryBuilder {

    public static final String SELECT_COUNT = """
        WITH user_stats AS (
            SELECT\s
                p.user_id,
                MAX(p.total_points) AS total_points,
                SUM(p.donated_value) AS total_donated_value
            FROM pontuations p
            GROUP BY p.user_id
        )
        SELECT COUNT(*)
        FROM user_stats s
        JOIN users u ON u.id = s.user_id
        WHERE (
            unaccent(u.name) ILIKE COALESCE('%%' || unaccent(?) || '%%', u.name)
            OR unaccent(u.email) ILIKE COALESCE('%%' || unaccent(?) || '%%', u.email)
            OR unaccent(u.phone) ILIKE COALESCE('%%' || unaccent(?) || '%%', u.phone)
        )
        AND (
            COALESCE(?, '') = '' OR
            CASE
                WHEN s.total_points >= 1000 THEN 'margarida'
                WHEN s.total_points >= 500 THEN 'broto'
                ELSE 'semente'
            END = ?
        );
   \s""";

    public static final String GET_DONORS = """
        WITH user_stats AS (
            SELECT 
                p.user_id,
                MAX(p.total_points) AS total_points,
                SUM(p.donated_value) AS total_donated_value
            FROM pontuations p
            GROUP BY p.user_id
        )
        SELECT 
            u.id,
            u.name,
            u.email,
            u.phone,
            s.total_points,
            s.total_donated_value,
            CASE
                WHEN s.total_points >= 1000 THEN 'margarida'
                WHEN s.total_points >= 500 THEN 'broto'
                ELSE 'semente'
            END AS badge
        FROM user_stats s
        JOIN users u ON u.id = s.user_id
        WHERE (
            unaccent(u.name) ILIKE COALESCE('%%' || unaccent(?) || '%%', u.name)
            OR unaccent(u.email) ILIKE COALESCE('%%' || unaccent(?) || '%%', u.email)
            OR unaccent(u.phone) ILIKE COALESCE('%%' || unaccent(?) || '%%', u.phone)
        )
        AND (
            COALESCE(?, '') = '' OR
            CASE
                WHEN s.total_points >= 1000 THEN 'margarida'
                WHEN s.total_points >= 500 THEN 'broto'
                ELSE 'semente'
            END = ?
        )
        ORDER BY
            CASE WHEN ? = 'name' THEN u.name END,
            CASE WHEN ? = 'email' THEN u.email END,
            CASE WHEN ? = 'phone' THEN u.phone END,
            CASE WHEN ? = 'total_points' THEN s.total_points END,
            CASE WHEN ? = 'total_donated_value' THEN s.total_donated_value END
        %s
        LIMIT ? OFFSET ?;
    """;
}