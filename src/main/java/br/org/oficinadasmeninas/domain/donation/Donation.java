package br.org.oficinadasmeninas.domain.donation;

import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.paymentgateway.PaymentGatewayEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public class Donation {

	private UUID id;
	private double value;
	private Double fee;
	private Double valueLiquid;
	private String checkoutId;
	private PaymentGatewayEnum gateway;
	private UUID sponsorshipId;
	private PaymentMethodEnum method;
    private String cardBrand;
	private UUID userId;
	private LocalDateTime donationAt;

	public Donation() {
	}

	public Donation(UUID id, double value, String checkoutId, PaymentGatewayEnum gateway,
			UUID sponsorshipId, PaymentMethodEnum method, UUID userId, LocalDateTime donationAt) {
		super();
		this.id = id;
		this.value = value;
		this.checkoutId = checkoutId;
		this.gateway = gateway;
		this.sponsorshipId = sponsorshipId;
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

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
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

	public UUID getSponsorshipId() {
		return sponsorshipId;
	}

	public void setSponsorshipId(UUID sponsorshipId) {
		this.sponsorshipId = sponsorshipId;
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

	public Double getValueLiquid() { return valueLiquid; }

	public void setValueLiquid(Double valueLiquid) { this.valueLiquid = valueLiquid; }

    public String getCardBrand() { return cardBrand; }

    public void setCardBrand(String cardBrand) { this.cardBrand = cardBrand; }

}
