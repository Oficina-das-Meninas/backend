package br.org.oficinadasmeninas.domain.donation;

import java.time.LocalDateTime;
import java.util.UUID;

public class Donation {
    private UUID id;
    private long value;
    private LocalDateTime donationAt;
    private String idCheckout;
    private PaymentMethodEnum paymentMethod;

    public Donation() {}

    public Donation(UUID id, long value, LocalDateTime donationAt, String idCheckout, PaymentMethodEnum paymentMethod) {
        this.id = id;
        this.value = value;
        this.donationAt = donationAt;
        this.idCheckout = idCheckout;
        this.paymentMethod = paymentMethod;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public LocalDateTime getDonationAt() {
        return donationAt;
    }

    public void setDonationAt(LocalDateTime donationAt) {
        this.donationAt = donationAt;
    }

    public String getIdCheckout() {
        return idCheckout;
    }

    public void setIdCheckout(String idCheckout) {
        this.idCheckout = idCheckout;
    }

    public PaymentMethodEnum getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodEnum paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
