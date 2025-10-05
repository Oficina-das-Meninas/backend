package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

import br.org.oficinadasmeninas.domain.payment.dto.CustomerDto;
import br.org.oficinadasmeninas.domain.payment.dto.ItemDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentMethodDto;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.PaymentsMethodEnum;

import java.util.List;

public record RequestCreateCheckoutPagbank(
        String externalId,
        String expiration_date,
        RequestCreateCheckoutCustomer customer,
        List<RequestCreateCheckoutItem> items,
        List<RequestCreateCheckoutPaymentMethod> payment_methods,
        String redirect_url,
        List<String> notification_urls,
        List<String> payment_notification_urls
) {}
