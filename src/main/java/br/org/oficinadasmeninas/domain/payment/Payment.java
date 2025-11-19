package br.org.oficinadasmeninas.domain.payment;

import br.org.oficinadasmeninas.domain.paymentgateway.PaymentGatewayEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {

	private UUID id;
	private UUID donationId;
	private PaymentGatewayEnum gateway;
	private String checkoutId;
	private PaymentMethodEnum method;
	private PaymentStatusEnum status;
	private LocalDateTime date;

	public Payment(UUID id, UUID donationId, PaymentGatewayEnum gateway, String checkoutId, PaymentMethodEnum method,
			PaymentStatusEnum status, LocalDateTime date) {
		super();
		this.id = id;
		this.donationId = donationId;
		this.gateway = gateway;
		this.checkoutId = checkoutId;
		this.method = method;
		this.status = status;
		this.date = date;
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
