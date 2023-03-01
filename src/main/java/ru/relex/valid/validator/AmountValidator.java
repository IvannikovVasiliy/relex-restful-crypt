package ru.relex.valid.validator;

import ru.relex.valid.constraint.AmountConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<AmountConstraint, Double> {

    @Override
    public void initialize(AmountConstraint currency) {
    }

    @Override
    public boolean isValid(Double amount,
                           ConstraintValidatorContext cxt) {
        return amount != null && amount > 0;
    }
}
