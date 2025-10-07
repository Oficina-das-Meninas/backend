package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

import br.org.oficinadasmeninas.domain.payment.dto.CustomerDto;
import br.org.oficinadasmeninas.domain.payment.dto.ItemDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentMethodDto;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.PaymentsMethodEnum;

import java.util.List;
import java.util.Optional;


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
