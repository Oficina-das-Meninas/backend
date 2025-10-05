package br.org.oficinadasmeninas.domain.paymentgateway.service;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.ResponseCreateCheckoutDto;

import java.util.UUID;

public interface IPaymentGatewayService {
    ResponseCreateCheckoutDto createCheckout(ResponseCreateCheckoutDto responseCreateCheckoutDto);
    void updatePaymentStatus(UUID paymentId, PaymentStatusEnum paymentStatus);
    void updateCheckoutStatus(String checkoutId, UUID paymentId);
}
