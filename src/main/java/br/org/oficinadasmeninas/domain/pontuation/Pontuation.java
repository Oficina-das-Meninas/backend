package br.org.oficinadasmeninas.domain.pontuation;

import java.time.LocalDateTime;
import java.util.UUID;

public class Pontuation {

    private UUID id;
    private UUID userId;
    private UUID paymentId;
    private double donatedValue;
    private LocalDateTime donatedDate;
    private Long earnedPoints;
    private Long totalPoints;
    private String method;
    private int recurrenceSequence;
    private boolean isFirstDonation;

    public Pontuation(
            UUID id,
            UUID userId,
            UUID paymentId,
            double donatedValue,
            LocalDateTime donatedDate,
            Long earnedPoints,
            Long totalPoints,
            String method,
            int recurrenceSequence,
            boolean isFirstDonation
    ) {
        this.id = id;
        this.userId = userId;
        this.paymentId = paymentId;
        this.donatedValue = donatedValue;
        this.donatedDate = donatedDate;
        this.earnedPoints = earnedPoints;
        this.totalPoints = totalPoints;
        this.method = method;
        this.recurrenceSequence = recurrenceSequence;
        this.isFirstDonation = isFirstDonation;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public double getDonatedValue() {
        return donatedValue;
    }

    public LocalDateTime getDonatedDate() {
        return donatedDate;
    }

    public Long getEarnedPoints() {
        return earnedPoints;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getRecurrenceSequence() {
        return recurrenceSequence;
    }

    public boolean isFirstDonation() {
        return isFirstDonation;
    }
}
