package br.org.oficinadasmeninas.domain.donation;

import java.time.LocalDateTime;
import java.util.UUID;

public class Donation {
    private UUID id;
    private long value;
    private LocalDateTime donationAt;
    private String idCheckout;
    private PaymentMethodEnum paymentMethod;

}
