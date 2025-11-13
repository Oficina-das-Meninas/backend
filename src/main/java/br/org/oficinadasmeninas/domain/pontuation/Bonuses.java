package br.org.oficinadasmeninas.domain.pontuation;

public class Bonuses {
    private long perFirstDonation;
    private long perRecurrence;
    private long perDonatedValue;

    public void setFirstDonationBonus(long amount) {
        perFirstDonation = amount;
    }

    public void setRecurrenceBonus(long amount) {
        perRecurrence = amount;
    }

    public void setValueBonus(long amount) {
        perDonatedValue = amount;
    }
}