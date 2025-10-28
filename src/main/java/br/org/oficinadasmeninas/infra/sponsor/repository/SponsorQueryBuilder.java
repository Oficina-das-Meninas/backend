package br.org.oficinadasmeninas.infra.sponsor.repository;

public final class SponsorQueryBuilder {

    public static final String INSERT_SPONSOR = """
        INSERT INTO sponsors (
            id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String UPDATE_SPONSOR = """
        UPDATE sponsors
        SET sponsorUntil = ?
           ,isActive = ?
           ,subscriptionId = ?
        WHERE id = ?
    """;

    public static final String ACTIVATE_SPONSOR_BY_USER_ID = """
        UPDATE sponsors
        SET isActive = ?
        WHERE id = (
            SELECT lastId
            FROM (
                SELECT id AS lastId
                FROM sponsors
                WHERE userId = ?
                ORDER BY id DESC
                LIMIT 1
            ) AS sub
        )
    """;

    public static final String SUSPEND_SPONSOR_BY_ID = """
        UPDATE sponsors
        SET sponsorUntil = ?
            ,isActive = ?
        WHERE id = ?
    """;

    public static final String GET_SPONSOR_BY_ID = """
        SELECT id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId
        FROM sponsors
        WHERE id = ?
    """;

    public static final String GET_SPONSOR_BY_USER_ID = """    
        SELECT id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId
        FROM sponsors
        WHERE userId = ?
    """;

    public static final String GET_ACTIVE_SPONSOR_BY_USER_ID = """    
        SELECT id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId
        FROM sponsors
        WHERE userId = ?
        AND isActive = true
    """;

    public static final String GET_SPONSOR_BY_SUBSCRIPTION_ID = """        
        SELECT id, monthlyAmount, billingDay, userId, sponsorSince, sponsorUntil, isActive, subscriptionId
        FROM sponsors WHERE
        subscriptionId = ?
    """;
}