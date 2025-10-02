package br.org.oficinadasmeninas.infra.facade;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.service.IPaymentService;

@Service
public class GatewayNotificationFacade {

	private final IDonationService donationService;
	private final IPaymentService paymentService;

	public GatewayNotificationFacade(IDonationService donationService, IPaymentService paymentService) {
		super();
		this.donationService = donationService;
		this.paymentService = paymentService;
	}

	public void updatePaymentStatus(UUID paymentId, PaymentStatusEnum paymentStatus) {
	}

	public void updateCheckoutStatus(String checkoutId, UUID paymentId) {
	}

}
