package br.org.oficinadasmeninas.presentation.shared.notations;

import br.org.oficinadasmeninas.presentation.shared.validator.DonationItemValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

public class DonationNotation {

    @Documented
    @Constraint(validatedBy = DonationItemValidator.class)
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidDonationItem {
        String message() default "Atributo cycle obrigatório para doações recorrentes";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
}
