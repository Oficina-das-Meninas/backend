package br.org.oficinadasmeninas.infra.pontuation.repository;

public class PontuationQueryBuilder {
    public static final String SELECT_COUNT = """
        SELECT count(*)
        FROM PONTUATIONS
        WHERE user_id = ?
          AND method ILIKE COALESCE('%' || ? || '%', method)
          AND donated_date BETWEEN COALESCE(?, donated_date)
                           AND COALESCE(?, donated_date)
    """;

    public static final String GET_FILTERED_PONTUATIONS = """
        SELECT id, user_id, payment_id, donated_value, donated_date, earned_points, total_points, method, recurrence_sequence, is_first_donation
        FROM PONTUATIONS
        WHERE user_id = ?
          AND method ILIKE COALESCE('%' || ? || '%', method)
          AND donated_date BETWEEN COALESCE(?, donated_date)
                           AND COALESCE(?, donated_date)
        ORDER BY donated_date DESC
        LIMIT ? OFFSET ?;
    """;
}