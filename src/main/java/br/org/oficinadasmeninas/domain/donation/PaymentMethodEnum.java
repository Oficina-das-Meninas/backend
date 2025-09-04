package br.org.oficinadasmeninas.domain.donation;

public enum PaymentMethodEnum {
    CREDIT_CARD("CREDIT_CARD");

    private final String name;

    PaymentMethodEnum(String name) {
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
