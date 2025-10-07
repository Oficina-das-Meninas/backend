package br.org.oficinadasmeninas.application;

import br.org.oficinadasmeninas.domain.donation.dto.*;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.dto.*;
import br.org.oficinadasmeninas.domain.paymentgateway.PaymentGatewayEnum;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.*;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutDto;
import br.org.oficinadasmeninas.domain.paymentgateway.service.IPaymentGatewayService;
import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import br.org.oficinadasmeninas.domain.payment.service.IPaymentService;

@Service
public class DonationApplication {

	private final IDonationService donationService;
	private final IPaymentService paymentService;
	private final IPaymentGatewayService paymentGatewayService;

	public DonationApplication(IDonationService donationService, IPaymentService paymentService,
                               IPaymentGatewayService paymentGatewayService) {
		this.donationService = donationService;
		this.paymentService = paymentService;
        this.paymentGatewayService = paymentGatewayService;
	}

	public DonationCheckoutDto createDonationCheckout(CreateDonationCheckoutDto donationCheckout) {
		CreateDonationDto createDonation = new CreateDonationDto(donationCheckout.donation().value(),
				donationCheckout.donor().id(), DonationStatusEnum.PENDING);
		DonationDto donation = donationService.createDonation(createDonation);

        ResponseCreateCheckoutDto checkout = paymentGatewayService.createCheckout(createPaymentGateway(donation.id().toString(), donationCheckout.donor(), donationCheckout.donation()));

        PaymentGatewayEnum paymentGatewayEnum = donationCheckout.donation().gatewayPayment();

        System.out.println(paymentGatewayEnum.toString());

		CreatePaymentDto createPayment = new CreatePaymentDto(donation.id(), paymentGatewayEnum, checkout.checkoutId(), null, PaymentStatusEnum.WAITING);
		paymentService.createPayment(createPayment);

		return new DonationCheckoutDto(checkout.link());
	}
//    String name, String phone, String email, String document
    private RequestCreateCheckoutDto createPaymentGateway(String donationId, DonorInfoDto customer, DonationItemDto donation){
        return new RequestCreateCheckoutDto(
                donationId,
                new RequestCreateCheckoutCustomerDto(customer.name(), customer.phone(), customer.email(), customer.document()),
                new RequestCreateCheckoutSignatureDto(donation.isRecurring(), donation.cycles()),
                new RequestCreateCheckoutDonationDto(donation.value())
        );
    }
}
