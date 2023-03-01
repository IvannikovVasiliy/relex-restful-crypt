package ru.relex.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @NotBlank
    @Size(min = 5, max = 50, message = "username length should be more 5 and less 25")
    private String username;
    @Email(message = "Should be in format email")
    private String email;
    @NotBlank(message = "password should not be null or empty")
    private String password;
}
