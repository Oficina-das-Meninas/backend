package br.org.oficinadasmeninas.domain.payment.dto;

import java.util.List;

public record RequestCreateCheckoutDto(String reference_id,
        String expiration_date,
        CustomerDto  customer,
        List<ItemDto> items,
        List<PaymentMethodDto> payment_methods,
        String redirect_url,
        List<String> notification_urls,
        List<String> payment_notification_urls) {

}
