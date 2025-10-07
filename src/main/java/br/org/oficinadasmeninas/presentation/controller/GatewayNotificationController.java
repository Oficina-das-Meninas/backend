package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.paymentgateway.service.IPaymentGatewayService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.oficinadasmeninas.domain.payment.dto.CheckoutNotificationDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentNotificationDto;
import br.org.oficinadasmeninas.infra.facade.GatewayNotificationFacade;

@RestController
@RequestMapping("/api/nofitication")
public class GatewayNotificationController {

	private final IPaymentGatewayService paymentGatewayService;

	public GatewayNotificationController(IPaymentGatewayService paymentGatewayService) {
		super();
        this.paymentGatewayService = paymentGatewayService;
	}
	
	@PostMapping("/checkout")
    public void notifyCheckout(@RequestBody CheckoutNotificationDto request) {
		paymentGatewayService.updateCheckoutStatus(request.id(), request.reference_id());
    }

    @PostMapping("/payment")
    public void notifyPayment(@RequestBody PaymentNotificationDto request) {
    	paymentGatewayService.updatePaymentStatus(request.reference_id(), request.charges().getFirst().status());
    }

}
