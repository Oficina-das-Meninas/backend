package br.org.oficinadasmeninas.domain.donation;

import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.paymentgateway.PaymentGatewayEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public class Donation {

	private UUID id;
	private double value;
	private String checkoutId;
	private PaymentGatewayEnum gateway;
	private UUID sponsorId;
	private PaymentMethodEnum method;
	private UUID userId;
	private LocalDateTime donationAt;

	public Donation() {
	}

	public Donation(UUID id, double value, String checkoutId, PaymentGatewayEnum gateway,
			UUID sponsorId, PaymentMethodEnum method, UUID userId, LocalDateTime donationAt) {
		super();
		this.id = id;
		this.value = value;
		this.checkoutId = checkoutId;
		this.gateway = gateway;
		this.sponsorId = sponsorId;
		this.method = method;
		this.userId = userId;
		this.donationAt = donationAt;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getCheckoutId() {
		return checkoutId;
	}

	public void setCheckoutId(String checkoutId) {
		this.checkoutId = checkoutId;
	}

	public PaymentGatewayEnum getGateway() {
		return gateway;
	}

	public void setGateway(PaymentGatewayEnum gateway) {
		this.gateway = gateway;
	}

	public UUID getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(UUID sponsorId) {
		this.sponsorId = sponsorId;
	}

	public PaymentMethodEnum getMethod() {
		return method;
	}

	public void setMethod(PaymentMethodEnum method) {
		this.method = method;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public LocalDateTime getDonationAt() {
		return donationAt;
	}

	public void setDonationAt(LocalDateTime donationAt) {
		this.donationAt = donationAt;
	}

}
