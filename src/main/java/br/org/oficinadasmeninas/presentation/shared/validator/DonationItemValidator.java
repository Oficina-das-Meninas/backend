package br.org.oficinadasmeninas.presentation.shared.validator;

import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationCheckoutDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationItemDto;
import br.org.oficinadasmeninas.presentation.shared.notations.DonationNotation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DonationItemValidator implements ConstraintValidator<DonationNotation.ValidDonationItem, DonationItemDto> {

    @Override
    public boolean isValid(DonationItemDto dto, ConstraintValidatorContext context) {
        if (dto == null) return true;
        if (dto.isRecurring() && (dto.cycles().isEmpty())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Atributo cycle obrigatório para doações recorrentes")
                    .addPropertyNode("cycles")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
