package br.org.oficinadasmeninas.presentation.shared.validator;


import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationCheckoutDto;
import br.org.oficinadasmeninas.presentation.shared.notations.ValidDonationCheckout;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DonationCheckoutValidator implements ConstraintValidator<ValidDonationCheckout, CreateDonationCheckoutDto> {

    @Override
    public boolean isValid(CreateDonationCheckoutDto dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        var donor = dto.donor();
        var donation = dto.donation();

        if (donation != null && donation.isRecurring()) {

            if (donor == null || donor.id() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("O donor.id é obrigatório para doações recorrentes")
                        .addPropertyNode("donor.id")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
