package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.PaymentChargesDto;
import br.org.oficinadasmeninas.domain.paymentgateway.service.IPaymentGatewayService;
import br.org.oficinadasmeninas.infra.donation.service.DonationService;
import br.org.oficinadasmeninas.infra.payment.service.PaymentService;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseWebhookCustomer;
import br.org.oficinadasmeninas.infra.sponsor.service.SponsorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.oficinadasmeninas.domain.payment.dto.CheckoutNotificationDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentNotificationDto;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/nofitication")
public class GatewayNotificationController {

	private final IPaymentGatewayService paymentGatewayService;
    private final ObjectMapper mapper = new ObjectMapper();
    private final DonationService donationService;
    private final SponsorService sponsorService;
    private final PaymentService paymentService;

    public GatewayNotificationController(IPaymentGatewayService paymentGatewayService, DonationService donationService, SponsorService sponsorService, PaymentService paymentService) {
		super();
        this.paymentGatewayService = paymentGatewayService;
        this.donationService = donationService;
        this.sponsorService = sponsorService;
        this.paymentService = paymentService;
    }
	
	@PostMapping("/checkout")
    public void notifyCheckout(@RequestBody CheckoutNotificationDto request) {
		paymentGatewayService.updateCheckoutStatus(request.id(), request.reference_id(), request.status());
    }

    @PostMapping("/payment")
    public void notifyPayment(@RequestBody PaymentNotificationDto request) throws IOException {
        PaymentChargesDto charge = request.charges().getFirst();
        saveLog(charge);
        boolean recurring = charge.recurring() != null;
        ResponseWebhookCustomer customer = request.customer();
    	paymentGatewayService.updatePaymentStatus(request.reference_id(), charge.status(), charge.payment_method().type(), recurring, customer);

        if (charge.status() == PaymentStatusEnum.PAID){
            PaymentDto payment = paymentService.getPaymentsByDonation(request.reference_id()).getLast();
            paymentGatewayService.cancelCheckout(payment.checkoutId());
        }
    }

    private void saveLog(Object object) throws IOException {
        try (FileWriter writer = new FileWriter("webhook.json", true)) {
            writer.write(mapper.writeValueAsString(object));
            writer.write(System.lineSeparator());
        }
    }
}
