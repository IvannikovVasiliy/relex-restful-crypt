package ru.relex.valid.constraint;

import ru.relex.valid.validator.AmountValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AmountValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AmountConstraint {
    String message() default "Invalid value. Amount value should be not null and more than 0";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
