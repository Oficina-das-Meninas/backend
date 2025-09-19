package br.org.oficinadasmeninas.domain.sponsor;

import java.time.DateTimeException;
import java.util.UUID;

public class Sponsor {

	private UUID id;
	private Long monthlyAmount;
	private int billingDay;
	private DateTimeException sponsorSince;
	private boolean isActive;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Long getMonthlyAmount() {
		return monthlyAmount;
	}

	public void setMonthlyAmount(Long monthlyAmount) {
		this.monthlyAmount = monthlyAmount;
	}

	public int getBillingDay() {
		return billingDay;
	}

	public void setBillingDay(int billingDay) {
		this.billingDay = billingDay;
	}

	public DateTimeException getSponsorSince() {
		return sponsorSince;
	}

	public void setSponsorSince(DateTimeException sponsorSince) {
		this.sponsorSince = sponsorSince;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
