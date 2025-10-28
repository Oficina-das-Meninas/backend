package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

 import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;

import java.util.List;

public record RequestCreateCheckoutConfig(
        String expirationDate,
        String redirectUrl,
        String itemName,
        int quantity,
        List<String> notificationUrls,
        List<String> paymentNotificationUrls,
        List<PaymentMethodEnum> paymentMethods,
        String itemImage,
        int cycles,
        RequestCreateCheckoutRecurrenceInterval interval
) {}
