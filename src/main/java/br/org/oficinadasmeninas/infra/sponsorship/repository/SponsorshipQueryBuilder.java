package br.org.oficinadasmeninas.infra.sponsorship.repository;

public final class SponsorshipQueryBuilder {

    public static final String INSERT_SPONSORSHIP = """
        INSERT INTO sponsorships (
            id, billing_day, start_date, is_active, subscription_id, user_id, cancel_date
        )
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String UPDATE_SPONSORSHIP = """
        UPDATE sponsorships
        SET cancel_date = ?
           ,is_active = ?
           ,subscription_id = ?
        WHERE id = ?
    """;

    public static final String ACTIVATE_SPONSORSHIP_BY_USER_ID = """
        UPDATE sponsorships
        SET is_active = ?
        WHERE id = (
            SELECT lastId
            FROM (
                SELECT id AS lastId
                FROM sponsorships
                WHERE user_id = ?
                ORDER BY id DESC
                LIMIT 1
            ) AS sub
        )
    """;

    public static final String CANCEL_SPONSORSHIP_BY_ID = """
        UPDATE sponsorships
        SET cancel_date = ?
            ,is_active = ?
        WHERE id = ?
    """;

    public static final String GET_SPONSORSHIP_BY_ID = """
        SELECT id, billing_day, start_date, is_active, subscription_id, user_id, cancel_date
        FROM sponsorships
        WHERE id = ?
    """;

    public static final String GET_SPONSORSHIP_BY_USER_ID = """    
        SELECT id, billing_day, start_date, is_active, subscription_id, user_id, cancel_date
        FROM sponsorships
        WHERE user_id = ?
    """;

    public static final String GET_ACTIVE_SPONSORSHIP_BY_USER_ID = """    
        SELECT id, billing_day, start_date, is_active, subscription_id, user_id, cancel_date
        FROM sponsorships
        WHERE user_id = ?
        AND is_active = true
    """;

    public static final String GET_SPONSORSHIP_BY_SUBSCRIPTION_ID = """        
        SELECT id, billing_day, start_date, is_active, subscription_id, user_id, cancel_date
        FROM sponsorships WHERE
        subscription_id = ?
    """;
}