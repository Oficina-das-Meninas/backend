package br.org.oficinadasmeninas.presentation.shared.validator;

import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationCheckoutDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationItemDto;
import br.org.oficinadasmeninas.presentation.shared.notations.DonationNotation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DonationItemValidator implements ConstraintValidator<DonationNotation.ValidDonationItem, CreateDonationCheckoutDto> {

    @Override
    public boolean isValid(CreateDonationCheckoutDto dto, ConstraintValidatorContext context) {
        if (dto == null) return true;
        System.out.println(dto.toString());
        if (dto.donation().isRecurring() && (dto.donation().cycles().isEmpty())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Atributo cycle obrigatório para doações recorrentes")
                    .addPropertyNode("cycles")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
