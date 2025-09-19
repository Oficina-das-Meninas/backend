package br.org.oficinadasmeninas.infra.donation;


public enum PaymentDonationStatusEnum {
	
	PAID("PAID"),
	IN_ANALYSIS("IN_ANALYSIS"), 
	DECLINED("DECLINED"), 
	CANCELED("CANCELED"), 
	WAITING("WAITING");

	private final String name;

	private PaymentDonationStatusEnum(String name) {
		this.name = name;
	}

	public boolean equals(String otherName) {
		return name.equals(otherName);
	}

	public String toString() {
		return this.name;
	}
	
}
