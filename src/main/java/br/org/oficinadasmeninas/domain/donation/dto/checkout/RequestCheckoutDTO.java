package br.org.oficinadasmeninas.domain.donation.dto.checkout;

import java.util.List;

public record RequestCheckoutDTO(
        String reference_id,
        String expiration_date,
        UserCheckoutDTO user,
        List<ItemsCheckoutDTO> items,
        String redirect_uri,
        List<PaymentMethodCheckoutDTO> payment_methods,
        List<String> notification_urls,
        List<String> payment_notification_urls
){}