package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.donation.dto.DonationDto;
import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.dto.CheckoutNotificationDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentNotificationDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.PaymentChargesDto;
import br.org.oficinadasmeninas.domain.paymentgateway.service.IPaymentGatewayService;
import br.org.oficinadasmeninas.infra.logspagbank.dto.CreateLogPagbank;
import br.org.oficinadasmeninas.infra.logspagbank.service.LogPagbankService;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseWebhookCustomer;
import br.org.oficinadasmeninas.infra.shared.utils.JsonLogger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("isAnonymous()")
public class GatewayNotificationController {

	private final IPaymentGatewayService paymentGatewayService;
    private final IDonationService donationService;
    private final LogPagbankService logService;

    public GatewayNotificationController(IPaymentGatewayService paymentGatewayService, IDonationService donationService, LogPagbankService logService) {
		super();
        this.paymentGatewayService = paymentGatewayService;
        this.donationService = donationService;
        this.logService = logService;
    }
	
	@PostMapping("/checkout")
    public void notifyCheckout(@RequestBody CheckoutNotificationDto request) {
		JsonLogger.logRequest("WEBHOOK - POST /api/notifications/checkout", request);
		paymentGatewayService.updateCheckoutStatus(request.id(), request.reference_id(), request.status());
    }

    @PostMapping("/payment")
    public void notifyPayment(@RequestBody PaymentNotificationDto request) throws IOException {
    	JsonLogger.logRequest("WEBHOOK - POST /api/notifications/payment", request);
        PaymentChargesDto charge = request.charges().getFirst();
        saveLog(request);
        boolean recurring = charge.recurring() != null;
        ResponseWebhookCustomer customer = request.customer();
    	paymentGatewayService.updatePaymentStatus(request.reference_id(), charge.status(), charge.payment_method().type(), recurring, customer);
    	

        if (charge.status() == PaymentStatusEnum.PAID){
            DonationDto donation = donationService.findById(request.reference_id());
            if (donation.checkoutId() != null) {
                paymentGatewayService.cancelCheckout(donation.checkoutId());
            }
        }
    }

    private void saveLog(Object object) throws IOException {
    	logService.createLogPagbank(new CreateLogPagbank(
    				"WEBHOOK NOTIFY BODY",
    				LocalDateTime.now(),
    				object
    			));
    }
}
