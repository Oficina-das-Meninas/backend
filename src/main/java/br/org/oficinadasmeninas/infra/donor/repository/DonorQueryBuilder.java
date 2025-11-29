package br.org.oficinadasmeninas.infra.donor.repository;

import java.util.Map;

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
        JOIN account a ON a.id = u.account_id
        WHERE (
            unaccent(a.name) ILIKE COALESCE('%%' || unaccent(?) || '%%', a.name)
            OR unaccent(a.email) ILIKE COALESCE('%%' || unaccent(?) || '%%', a.email)
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

    public static final Map<String, String> ALLOWED_SORT_FIELDS = Map.of(
            "name", "a.name",
            "email", "a.email",
            "points", "s.total_points",
            "totalDonated", "s.total_donated_value"
    );

    public static final String GET_DONORS = """
        WITH user_stats AS (
            SELECT\s
                p.user_id,
                MAX(p.total_points) AS total_points,
                SUM(p.donated_value) AS total_donated_value
            FROM pontuations p
            GROUP BY p.user_id
        )
        SELECT\s
            u.id,
            u.account_id,
            a.name,
            a.email,
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
        JOIN account a ON a.id = u.account_id
        WHERE (
            unaccent(a.name) ILIKE COALESCE('%%' || unaccent(?) || '%%', a.name)
            OR unaccent(a.email) ILIKE COALESCE('%%' || unaccent(?) || '%%', a.email)
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
        %ORDER_BY%
        LIMIT ? OFFSET ?;
   \s""";
}