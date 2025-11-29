package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import br.org.oficinadasmeninas.domain.payment.dto.CheckoutNotificationDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentNotificationDto;
import br.org.oficinadasmeninas.domain.paymentgateway.service.IPaymentGatewayService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("isAnonymous()")
public class GatewayNotificationController {

	private final IPaymentGatewayService paymentGatewayService;

    public GatewayNotificationController(IPaymentGatewayService paymentGatewayService, IDonationService donationService) {
		super();
        this.paymentGatewayService = paymentGatewayService;
    }
	
	@PostMapping("/checkout")
    public void notifyCheckout(@RequestBody CheckoutNotificationDto request) {
		paymentGatewayService.notifyCheckout(request);
    }

    @PostMapping("/payment")
    public void notifyPayment(@RequestBody PaymentNotificationDto request) {
       paymentGatewayService.notifyPayment(request);
    }
}
