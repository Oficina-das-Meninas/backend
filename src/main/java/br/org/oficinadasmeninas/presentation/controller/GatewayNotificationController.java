package br.org.oficinadasmeninas.presentation.controller;

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

	private final GatewayNotificationFacade gatewayNotificationFacade;

	public GatewayNotificationController(GatewayNotificationFacade gatewayNotificationFacade) {
		super();
		this.gatewayNotificationFacade = gatewayNotificationFacade;
	}
	
	@PostMapping("/checkout")
    public void notifyCheckout(@RequestBody CheckoutNotificationDto request) {
		gatewayNotificationFacade.updateCheckoutStatus(request.id(), request.reference_id());
    }

    @PostMapping("/payment")
    public void notifyPayment(@RequestBody PaymentNotificationDto request) {
    	gatewayNotificationFacade.updatePaymentStatus(request.reference_id(), request.charges().getFirst().status());
    }

}
