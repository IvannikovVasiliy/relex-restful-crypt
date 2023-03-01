package ru.relex.valid.validator;

import ru.relex.valid.constraint.CurrencyConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CurrencyValidator implements ConstraintValidator<CurrencyConstraint, Double> {

    @Override
    public void initialize(CurrencyConstraint currency) {
    }

    @Override
    public boolean isValid(Double contactField,
                           ConstraintValidatorContext cxt) {
        return contactField > 0 && contactField <= Double.MAX_VALUE;
    }
}
