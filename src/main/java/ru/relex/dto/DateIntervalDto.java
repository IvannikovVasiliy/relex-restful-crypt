package ru.relex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.relex.valid.constraint.DateTimeIntervalConstraint;
import lombok.Data;

@Data
@DateTimeIntervalConstraint
public class DateIntervalDto {
    @JsonProperty("date_from")
    private String dateFrom;
    @JsonProperty("date_to")
    private String dateTo;
}
