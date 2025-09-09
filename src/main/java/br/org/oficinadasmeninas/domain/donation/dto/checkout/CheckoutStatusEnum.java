package br.org.oficinadasmeninas.domain.donation.dto.checkout;

public enum CheckoutStatusEnum {
    ACTIVE("ACTIVE"),
    EXPIRED("EXPIRED");

    private final String name;

    CheckoutStatusEnum(String name) {
        this.name = name;
    }

    public boolean equals(String otherName) {
        return name.equals(otherName);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
