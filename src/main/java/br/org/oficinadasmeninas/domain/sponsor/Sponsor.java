package br.org.oficinadasmeninas.domain.sponsor;

import java.time.LocalDateTime;
import java.util.UUID;

public class Sponsor {

	private UUID id;
	private double monthlyAmount;
	private int billingDay;
	private UUID userId;
	private LocalDateTime sponsorSince;
	private LocalDateTime sponsorUntil;
	private Boolean isActive;
	private String subscriptionId;

	public Sponsor() {
		super();
	}

	public Sponsor(UUID id, double monthlyAmount, int billingDay, UUID userId, LocalDateTime sponsorSince,
			LocalDateTime sponsorUntil, Boolean isActive, String subscriptionId) {
		super();
		this.id = id;
		this.monthlyAmount = monthlyAmount;
		this.billingDay = billingDay;
		this.userId = userId;
		this.sponsorSince = sponsorSince;
		this.sponsorUntil = sponsorUntil;
		this.isActive = isActive;
		this.subscriptionId = subscriptionId;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public double getMonthlyAmount() {
		return monthlyAmount;
	}

	public void setMonthlyAmount(double monthlyAmount) {
		this.monthlyAmount = monthlyAmount;
	}

	public int getBillingDay() {
		return billingDay;
	}

	public void setBillingDay(int billingDay) {
		this.billingDay = billingDay;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public LocalDateTime getSponsorSince() {
		return sponsorSince;
	}

	public void setSponsorSince(LocalDateTime sponsorSince) {
		this.sponsorSince = sponsorSince;
	}

	public LocalDateTime getSponsorUntil() {
		return sponsorUntil;
	}

	public void setSponsorUntil(LocalDateTime sponsorUntil) {
		this.sponsorUntil = sponsorUntil;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean active) {
		isActive = active;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
}
