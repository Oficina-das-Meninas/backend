package br.org.oficinadasmeninas.domain.donation;

import java.time.LocalDateTime;
import java.util.UUID;

public class Donation {

	private UUID id;
	private long value;
	private LocalDateTime donationAt;
	private UUID userId;
	private DonationStatusEnum status;

	public Donation() {
	}

	public Donation(UUID id, long value, LocalDateTime donationAt, UUID userId, DonationStatusEnum status) {
		super();
		this.id = id;
		this.value = value;
		this.donationAt = donationAt;
		this.userId = userId;
		this.status = status;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public LocalDateTime getDonationAt() {
		return donationAt;
	}

	public void setDonationAt(LocalDateTime donationAt) {
		this.donationAt = donationAt;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public DonationStatusEnum getStatus() {
		return status;
	}

	public void setStatus(DonationStatusEnum status) {
		this.status = status;
	}

}
