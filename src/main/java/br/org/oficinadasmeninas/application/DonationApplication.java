package br.org.oficinadasmeninas.application;

import java.util.List;

import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationCheckoutDto;
import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationCheckoutDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationDto;
import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import br.org.oficinadasmeninas.domain.payment.PaymentGatewayEnum;
import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.dto.CheckoutDto;
import br.org.oficinadasmeninas.domain.payment.dto.CreateCheckoutDto;
import br.org.oficinadasmeninas.domain.payment.dto.CreatePaymentDto;
import br.org.oficinadasmeninas.domain.payment.dto.CustomerDto;
import br.org.oficinadasmeninas.domain.payment.dto.CustomerPhoneDto;
import br.org.oficinadasmeninas.domain.payment.dto.ItemDto;
import br.org.oficinadasmeninas.domain.payment.service.IPaymentService;
import br.org.oficinadasmeninas.infra.facade.PaymentGatewayFacade;

@Service
public class DonationApplication {

	private final IDonationService donationService;
	private final IPaymentService paymentService;
	private final PaymentGatewayFacade paymentGatewayFacade;

	public DonationApplication(IDonationService donationService, IPaymentService paymentService,
			PaymentGatewayFacade paymentGatewayFacade) {
		this.donationService = donationService;
		this.paymentService = paymentService;
		this.paymentGatewayFacade = paymentGatewayFacade;
	}

	public DonationCheckoutDto createDonationCheckout(CreateDonationCheckoutDto donationCheckout) {
		CreateDonationDto createDonation = new CreateDonationDto(donationCheckout.donation().value(),
				donationCheckout.donor().id(), DonationStatusEnum.PENDING);
		DonationDto donation = donationService.createDonation(createDonation);

		CustomerPhoneDto customerPhone = new CustomerPhoneDto(donationCheckout.donor().phone().country(),
				donationCheckout.donor().phone().area(), donationCheckout.donor().phone().number());
		CustomerDto customer = new CustomerDto(donationCheckout.donor().name(), donationCheckout.donor().email(),
				donationCheckout.donor().document(), customerPhone);
		
		String itemName = donationCheckout.donation().isRecurring() ? "Apadrinhamento" : "Doação única"; 
		List<ItemDto> items = List.of(new ItemDto(itemName, 1, donationCheckout.donation().value(), null));
		CreateCheckoutDto createCheckout = new CreateCheckoutDto(donation.id().toString(), customer, items);

		CheckoutDto checkout = paymentGatewayFacade.createCheckout(createCheckout);
		
		//PaymentMethodEnum paymentMethod = PaymentMethodEnum.fromString(checkout.payment_methods().getFirst().type()); 
		CreatePaymentDto createPayment = new CreatePaymentDto(donation.id(), PaymentGatewayEnum.PAGBANK, checkout.id(), PaymentMethodEnum.CREDIT_CARD, PaymentStatusEnum.WAITING);
		paymentService.createPayment(createPayment);

		return new DonationCheckoutDto(checkout.links().getFirst().href());
	}

}
