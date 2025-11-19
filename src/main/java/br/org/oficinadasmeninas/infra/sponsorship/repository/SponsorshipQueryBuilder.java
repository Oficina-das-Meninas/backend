package br.org.oficinadasmeninas.infra.sponsorship.repository;

public final class SponsorshipQueryBuilder {

    public static final String INSERT_SPONSORSHIP = """
        INSERT INTO sponsorships (
            id, billingDay, startDate, isActive, subscriptionId, userId, cancelDate
        )
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String UPDATE_SPONSORSHIP = """
        UPDATE sponsorships
        SET cancelDate = ?
           ,isActive = ?
           ,subscriptionId = ?
        WHERE id = ?
    """;

    public static final String ACTIVATE_SPONSORSHIP_BY_USER_ID = """
        UPDATE sponsorships
        SET isActive = ?
        WHERE id = (
            SELECT lastId
            FROM (
                SELECT id AS lastId
                FROM sponsorships
                WHERE userId = ?
                ORDER BY id DESC
                LIMIT 1
            ) AS sub
        )
    """;

    public static final String CANCEL_SPONSORSHIP_BY_ID = """
        UPDATE sponsorships
        SET cancelDate = ?
            ,isActive = ?
        WHERE id = ?
    """;

    public static final String GET_SPONSORSHIP_BY_ID = """
        SELECT id, billingDay, startDate, isActive, subscriptionId, userId, cancelDate
        FROM sponsorships
        WHERE id = ?
    """;

    public static final String GET_SPONSORSHIP_BY_USER_ID = """    
        SELECT id, billingDay, startDate, isActive, subscriptionId, userId, cancelDate
        FROM sponsorships
        WHERE userId = ?
    """;

    public static final String GET_ACTIVE_SPONSORSHIP_BY_USER_ID = """    
        SELECT id, billingDay, startDate, isActive, subscriptionId, userId, cancelDate
        FROM sponsorships
        WHERE userId = ?
        AND isActive = true
    """;

    public static final String GET_SPONSORSHIP_BY_SUBSCRIPTION_ID = """        
        SELECT id, billingDay, startDate, isActive, subscriptionId, userId, cancelDate
        FROM sponsorships WHERE
        subscriptionId = ?
    """;
}