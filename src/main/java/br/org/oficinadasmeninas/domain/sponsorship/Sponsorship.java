package br.org.oficinadasmeninas.domain.sponsorship;

import java.time.LocalDateTime;
import java.util.UUID;

public class Sponsorship {

	private UUID id;
	private int billingDay;
	private LocalDateTime startDate;
	private Boolean isActive;
	private String subscriptionId;
	private UUID userId;
	private LocalDateTime cancelDate;

	public Sponsorship() {
		super();
	}

	public Sponsorship(UUID id, int billingDay, LocalDateTime startDate, Boolean isActive,
			String subscriptionId, UUID userId, LocalDateTime cancelDate) {
		super();
		this.id = id;
		this.billingDay = billingDay;
		this.startDate = startDate;
		this.isActive = isActive;
		this.subscriptionId = subscriptionId;
		this.userId = userId;
		this.cancelDate = cancelDate;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getBillingDay() {
		return billingDay;
	}

	public void setBillingDay(int billingDay) {
		this.billingDay = billingDay;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
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

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public LocalDateTime getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(LocalDateTime cancelDate) {
		this.cancelDate = cancelDate;
	}
}
