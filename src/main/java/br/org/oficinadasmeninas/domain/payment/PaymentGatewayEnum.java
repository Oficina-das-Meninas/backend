package br.org.oficinadasmeninas.domain.payment;

public enum PaymentGatewayEnum {

	PAGBANK("PAGBANK");
	
	private final String name;

	private PaymentGatewayEnum(String name) {
		this.name = name;
	}

	public boolean equals(String otherName) {
		return name.equals(otherName);
	}

	public String toString() {
		return this.name;
	}
	
}
