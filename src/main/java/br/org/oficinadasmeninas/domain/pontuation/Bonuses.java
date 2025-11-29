package br.org.oficinadasmeninas.domain.pontuation;

public class Bonuses {
    private long perFirstDonation;
    private long perRecurrence;
    private long perDonatedValue;

    public long getFirstDonationBonus() {
        return perFirstDonation;
    }

    public void setFirstDonationBonus(long amount) {
        perFirstDonation = amount;
    }

    public long getRecurrenceBonus() {
        return perRecurrence;
    }

    public void setRecurrenceBonus(long amount) {
        perRecurrence = amount;
    }

    public long getDonatedBonus() {
        return perDonatedValue;
    }

    public void setValueBonus(long amount) {
        perDonatedValue = amount;
    }
}