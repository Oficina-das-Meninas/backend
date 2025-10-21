package br.org.oficinadasmeninas.domain.paymentgateway.service;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.ResponseCreateCheckoutDto;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.PaymentsMethodEnum;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseWebhookCustomer;

import java.util.UUID;

public interface IPaymentGatewayService {
	ResponseCreateCheckoutDto createCheckout(RequestCreateCheckoutDto responseCreateCheckoutDto);

	void cancelCheckout(String checkoutId);

	void updatePaymentStatus(UUID paymentId, PaymentStatusEnum paymentStatus, PaymentsMethodEnum paymentMethod,
			boolean recurring, ResponseWebhookCustomer customer);

	void updateCheckoutStatus(String checkoutId, UUID paymentId, PaymentStatusEnum paymentStatus);
}
