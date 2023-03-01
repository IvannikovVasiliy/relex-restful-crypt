package ru.relex.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDto {
    @Size(min = 5, max = 50, message = "The length should be more than 5 and less than 50")
    private String username;
    @NotNull
    private String password;
}
