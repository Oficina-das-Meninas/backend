package br.org.oficinadasmeninas.presentation.dto.notification;

import br.org.oficinadasmeninas.infra.donation.PaymentDonationStatusEnum;

public class ChargesPaymentDTO {
	
	private String id;
	private  String reference_id;
	private PaymentDonationStatusEnum status;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getReference_id() {
		return reference_id;
	}
	
	public void setReference_id(String reference_id) {
		this.reference_id = reference_id;
	}
	
	public PaymentDonationStatusEnum getStatus() {
		return status;
	}
	
	public void setStatus(PaymentDonationStatusEnum status) {
		this.status = status;
	}
	
}
