package br.org.oficinadasmeninas.presentation.shared.notations;
import br.org.oficinadasmeninas.presentation.shared.validator.DonationCheckoutValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DonationCheckoutValidator.class)
@Documented
public @interface ValidDonationCheckout {
    String message() default "Validação de donation inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}