package br.org.oficinadasmeninas.infra.paymentgateway.pagbank;

import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;

public enum PaymentsMethodEnum {
    CREDIT_CARD("CREDIT_CARD"),
    PIX("PIX"),
    DEBIT_CARD("DEBIT_CARD"),
    BOLETO("BOLETO");

    private final String name;

    private PaymentsMethodEnum(String name) {
        this.name = name;
    }

    public boolean equals(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }

    public static PaymentMethodEnum fromString(String value) {
        if (value == null) {
            return PaymentMethodEnum.CREDIT_CARD;
        }
        try {
            return PaymentMethodEnum.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PaymentMethodEnum.CREDIT_CARD;
        }
    }
}
