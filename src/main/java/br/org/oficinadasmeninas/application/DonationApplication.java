package br.org.oficinadasmeninas.application;

import br.org.oficinadasmeninas.domain.donation.dto.*;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.dto.*;
import br.org.oficinadasmeninas.domain.paymentgateway.PaymentGatewayEnum;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.*;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutDto;
import br.org.oficinadasmeninas.domain.paymentgateway.service.IPaymentGatewayService;
import br.org.oficinadasmeninas.domain.sponsor.Sponsor;
import br.org.oficinadasmeninas.domain.sponsor.dto.CreateSponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.service.ISponsorService;
import br.org.oficinadasmeninas.infra.recaptcha.CaptchaService;
import br.org.oficinadasmeninas.infra.shared.exception.ActiveSubscriptionAlreadyExistsException;
import br.org.oficinadasmeninas.infra.shared.exception.InvalidCaptchaException;
import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.donation.service.IDonationService;
import br.org.oficinadasmeninas.domain.payment.service.IPaymentService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class DonationApplication {

	private final IDonationService donationService;
	private final IPaymentService paymentService;
	private final IPaymentGatewayService paymentGatewayService;
    private final ISponsorService sponsorService;
    private final CaptchaService captchaService;

	public DonationApplication(IDonationService donationService, IPaymentService paymentService,
                               IPaymentGatewayService paymentGatewayService, ISponsorService sponsorService, CaptchaService captchaService) {
		this.donationService = donationService;
		this.paymentService = paymentService;
        this.paymentGatewayService = paymentGatewayService;
        this.sponsorService = sponsorService;
        this.captchaService = captchaService;
	}

	public DonationCheckoutDto createDonationCheckout(CreateDonationCheckoutDto donationCheckout) {

        if(!this.captchaService.isCaptchaValid(donationCheckout.captchaToken())) {
            throw new InvalidCaptchaException();
        }

        if (donationCheckout.donation().isRecurring()){
            Optional<SponsorDto> sponsor = this.sponsorService.getActiveSponsorByUserId(donationCheckout.donor().id());

            if (sponsor.isPresent()) {
                throw new ActiveSubscriptionAlreadyExistsException("Usuário já possui assinatura ativa");
            }
            this.createSponsor(donationCheckout);
        }

		CreateDonationDto createDonation = new CreateDonationDto(donationCheckout.donation().value(),
				donationCheckout.donor().id(), DonationStatusEnum.PENDING);
		DonationDto donation = donationService.createDonation(createDonation);

        ResponseCreateCheckoutDto checkout = paymentGatewayService.createCheckout(createPaymentGateway(donation.id().toString(), donationCheckout.donor(), donationCheckout.donation()));

        PaymentGatewayEnum paymentGatewayEnum = PaymentGatewayEnum.PAGBANK;

		CreatePaymentDto createPayment = new CreatePaymentDto(donation.id(), paymentGatewayEnum, checkout.checkoutId(), null, PaymentStatusEnum.WAITING);
		paymentService.create(createPayment);

		return new DonationCheckoutDto(checkout.link());
	}

    private RequestCreateCheckoutDto createPaymentGateway(String donationId, DonorInfoDto customer, DonationItemDto donation){
        return new RequestCreateCheckoutDto(
                donationId,
                new RequestCreateCheckoutCustomerDto(customer.name(), customer.phone(), customer.email(), customer.document()),
                new RequestCreateCheckoutSignatureDto(donation.isRecurring(), donation.cycles()),
                new RequestCreateCheckoutDonationDto(donation.value())
        );
    }
    private UUID createSponsor(CreateDonationCheckoutDto donationCheckout) {
        var donor = donationCheckout.donor();
        var donation = donationCheckout.donation();
       return sponsorService.createSponsor(new SponsorDto(
                donation.value(),
                LocalDateTime.now().getDayOfMonth(),
                donor.id(),
                LocalDateTime.now(),
                null,
                false,
                null
        ));
    }
}
