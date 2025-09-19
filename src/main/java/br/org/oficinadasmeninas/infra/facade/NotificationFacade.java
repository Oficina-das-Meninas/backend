package br.org.oficinadasmeninas.infra.facade;

import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.infra.donation.PaymentDonationStatusEnum;

@Service
public class NotificationFacade {

	public void updatePaymentStatus(Long orderId, PaymentDonationStatusEnum paymentStatus) {
		//TO-DO
    }
	
}
