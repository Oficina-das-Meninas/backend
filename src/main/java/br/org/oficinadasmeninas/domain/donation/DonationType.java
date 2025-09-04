package br.org.oficinadasmeninas.domain.donation;

public enum DonationType {
    CREDIT_CARD("CREDIT_CARD");

    private final String name;

    DonationType(String name) {
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
