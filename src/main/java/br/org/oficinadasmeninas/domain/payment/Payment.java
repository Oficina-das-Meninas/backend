package br.org.oficinadasmeninas.domain.payment;

import java.util.UUID;

public class Payment {

	private UUID id;
	private UUID donationId;
	private PaymentGatewayEnum gateway;
	private String checkoutId;
	private PaymentMethodEnum method;
	private PaymentStatusEnum status;

	public Payment(UUID id, UUID donationId, PaymentGatewayEnum gateway, String checkoutId, PaymentMethodEnum method,
			PaymentStatusEnum status) {
		this.id = id;
		this.donationId = donationId;
		this.gateway = gateway;
		this.checkoutId = checkoutId;
		this.method = method;
		this.status = status;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getDonationId() {
		return donationId;
	}

	public void setDonationId(UUID donationId) {
		this.donationId = donationId;
	}

	public PaymentGatewayEnum getGateway() {
		return gateway;
	}

	public void setGateway(PaymentGatewayEnum gateway) {
		this.gateway = gateway;
	}

	public String getCheckoutId() {
		return checkoutId;
	}

	public void setCheckoutId(String checkoutId) {
		this.checkoutId = checkoutId;
	}

	public PaymentMethodEnum getMethod() {
		return method;
	}

	public void setMethod(PaymentMethodEnum method) {
		this.method = method;
	}

	public PaymentStatusEnum getStatus() {
		return status;
	}

	public void setStatus(PaymentStatusEnum status) {
		this.status = status;
	}

}
