package ru.relex.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CurrencyDto {
    @NotBlank
    private String currency;
}
