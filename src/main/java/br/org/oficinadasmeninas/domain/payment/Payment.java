package br.org.oficinadasmeninas.domain.payment;

import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {

	private UUID id;
	private LocalDateTime date;
	private PaymentStatusEnum status;
	private UUID donationId;

	public Payment() {
		super();
	}

	public Payment(UUID id, LocalDateTime date, PaymentStatusEnum status, UUID donationId) {
		super();
		this.id = id;
		this.date = date;
		this.status = status;
		this.donationId = donationId;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public PaymentStatusEnum getStatus() {
		return status;
	}

	public void setStatus(PaymentStatusEnum status) {
		this.status = status;
	}

	public UUID getDonationId() {
		return donationId;
	}

	public void setDonationId(UUID donationId) {
		this.donationId = donationId;
	}
}
