package ru.relex.valid.constraint;

import ru.relex.valid.validator.CurrencyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrencyConstraint {
    String message() default "Invalid value. Currency value should be more than 0 and less than it.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
