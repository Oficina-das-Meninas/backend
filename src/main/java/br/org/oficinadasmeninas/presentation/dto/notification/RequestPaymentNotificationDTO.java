package br.org.oficinadasmeninas.presentation.dto.notification;

import java.util.List;

public class RequestPaymentNotificationDTO {

	private String id;
	private Long referenceId;
	private List<ChargesPaymentDTO> charges;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public List<ChargesPaymentDTO> getCharges() {
		return charges;
	}

	public void setCharges(List<ChargesPaymentDTO> charges) {
		this.charges = charges;
	}

}
