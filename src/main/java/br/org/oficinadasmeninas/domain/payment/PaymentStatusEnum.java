package br.org.oficinadasmeninas.domain.payment;

public enum PaymentStatusEnum {

	ACTIVE("ACTIVE"), PAID("PAID"), IN_ANALYSIS("IN_ANALYSIS"), DECLINED("DECLINED"), CANCELED("CANCELED"), WAITING("WAITING"), EXPIRED("EXPIRED");

	private final String name;

	private PaymentStatusEnum(String name) {
		this.name = name;
	}

	public boolean equals(String otherName) {
		return name.equals(otherName);
	}

	public String toString() {
		return this.name;
	}

}
