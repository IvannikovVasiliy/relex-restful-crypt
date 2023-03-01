package ru.relex.valid.validator;

import ru.relex.dto.DateIntervalDto;
import ru.relex.exception.DateTimeFormatException;
import ru.relex.valid.constraint.DateTimeIntervalConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeIntervalValidator implements ConstraintValidator<DateTimeIntervalConstraint, DateIntervalDto> {

    @Override
    public void initialize(DateTimeIntervalConstraint currency) {
    }

    @Override
    public boolean isValid(DateIntervalDto dateIntervalDto,
                           ConstraintValidatorContext cxt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");

        Date dateFrom;
        Date dateTo;
        try {
            dateFrom = dateFormat.parse(dateIntervalDto.getDateFrom());
        } catch (ParseException e) {
            throw new DateTimeFormatException("Unparsable date date_from");
        }

        try {
            dateTo = dateFormat.parse(dateIntervalDto.getDateTo());
        } catch (ParseException e) {
            throw new DateTimeFormatException("Unparsable date date_to");
        }

        if (dateFrom.before(dateTo)) {
            return true;
        } else {
            throw new DateTimeFormatException("date_from should be less than date_to");
        }
    }
}