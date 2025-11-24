package br.org.oficinadasmeninas.application;

import br.org.oficinadasmeninas.domain.donation.dto.*;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.dto.*;
import br.org.oficinadasmeninas.domain.paymentgateway.PaymentGatewayEnum;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.*;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutDto;
import br.org.oficinadasmeninas.domain.paymentgateway.service.IPaymentGatewayService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.sponsorship.dto.SponsorshipDto;
import br.org.oficinadasmeninas.domain.sponsorship.service.ISponsorshipService;
import br.org.oficinadasmeninas.infra.recaptcha.CaptchaService;
import br.org.oficinadasmeninas.infra.shared.exception.ActiveSubscriptionAlreadyExistsException;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.exceptions.ValidationException;

import org.springframework.stereotype.Service;

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
    private final ISponsorshipService sponsorshipService;
    private final CaptchaService captchaService;

	public DonationApplication(IDonationService donationService, IPaymentService paymentService,
                               IPaymentGatewayService paymentGatewayService, ISponsorshipService sponsorshipService, CaptchaService captchaService) {
		this.donationService = donationService;
		this.paymentService = paymentService;
        this.paymentGatewayService = paymentGatewayService;
        this.sponsorshipService = sponsorshipService;
        this.captchaService = captchaService;
	}

	public DonationCheckoutDto createDonationCheckout(CreateDonationCheckoutDto donationCheckout) {

//        if(!this.captchaService.isCaptchaValid(donationCheckout.captchaToken())) {
//            throw new ValidationException("Token Captcha inválido");
//        }

        UUID sponsorshipId = null;
        if (donationCheckout.donation().isRecurring()){
            Optional<SponsorshipDto> sponsorship = this.sponsorshipService.findActiveByUserId(donationCheckout.donor().id());

            if (sponsorship.isPresent()) {
                throw new ActiveSubscriptionAlreadyExistsException();
            }
            sponsorshipId = this.createSponsorship(donationCheckout);
        }

        ResponseCreateCheckoutDto checkout = paymentGatewayService.createCheckout(createPaymentGateway(donationCheckout.donor(), donationCheckout.donation()));

        PaymentGatewayEnum paymentGatewayEnum = PaymentGatewayEnum.PAGBANK;

		CreateDonationDto createDonation = new CreateDonationDto(
            donationCheckout.donation().value(),
            checkout.checkoutId(),
            paymentGatewayEnum,
            sponsorshipId,
            null,
            donationCheckout.donor().id(),
            checkout.referenceId()
        );
		DonationDto donation = donationService.insert(createDonation);

		CreatePaymentDto createPayment = new CreatePaymentDto(PaymentStatusEnum.WAITING, donation.id());
		paymentService.insert(createPayment);

		return new DonationCheckoutDto(checkout.link());
	}

    private RequestCreateCheckoutDto createPaymentGateway(DonorInfoDto customer, DonationItemDto donation){
        return new RequestCreateCheckoutDto(
                UUID.randomUUID(),
                new RequestCreateCheckoutCustomerDto(customer.name(), customer.phone(), customer.email(), customer.document()),
                new RequestCreateCheckoutSignatureDto(donation.isRecurring(), donation.cycles()),
                new RequestCreateCheckoutDonationDto(donation.value())
        );
    }
    private UUID createSponsorship(CreateDonationCheckoutDto donationCheckout) {
        var donor = donationCheckout.donor();
       return sponsorshipService.insert(new SponsorshipDto(
                null,
                LocalDateTime.now().getDayOfMonth(),
                LocalDateTime.now(),
                false,
                null,
                donor.id(),
                null
        ));
    }

    public void cancelRecurringDonationSubscription(UUID donationId) {
        DonationDto donationDto = donationService.findById(donationId);

        if (donationDto.sponsorshipId() == null) {
            throw new ValidationException(Messages.DONATION_IS_NOT_RECURRING);
        }

        Optional<SponsorshipDto> sponsorshipDto = sponsorshipService.findById(donationDto.sponsorshipId());
        if (sponsorshipDto.isEmpty() || sponsorshipDto.get().subscriptionId() == null || !sponsorshipDto.get().isActive()) {
            throw new NotFoundException(Messages.RECURRING_DONATION_SUBSCRIPTION_NOT_FOUND);
        }

        paymentGatewayService.cancelRecurringDonationSubscription(
                sponsorshipDto.get().subscriptionId()
        );

        sponsorshipService.cancelSponsorship(sponsorshipDto.get().id());
    }
}
