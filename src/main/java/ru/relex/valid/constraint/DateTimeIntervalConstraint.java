package ru.relex.valid.constraint;

import ru.relex.valid.validator.DateTimeIntervalValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateTimeIntervalValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeIntervalConstraint {
    String message() default "Invalid value. Date should be in past.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
