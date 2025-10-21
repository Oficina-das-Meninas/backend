package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

import java.util.List;


public record RequestCreateCheckoutPagbank(
        String reference_id,
        String expiration_date,
        RequestCreateCheckoutCustomer customer,
        List<RequestCreateCheckoutItem> items,
        RequestCreateCheckoutRecurrence recurrence_plan,
        List<RequestCreateCheckoutPaymentMethod> payment_methods,
        String redirect_url,
        List<String> notification_urls,
        List<String> payment_notification_urls
) {}
