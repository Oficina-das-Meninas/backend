package br.org.oficinadasmeninas.domain.donation;

public enum DonationStatusEnum {
	
	PAID("PAID"), PENDING("PENDING"), CANCELED("CANCELED");

	private final String name;

	private DonationStatusEnum(String name) {
		this.name = name;
	}

	public boolean equals(String otherName) {
		return name.equals(otherName);
	}

	public String toString() {
		return this.name;
	}
}
