package ru.relex.service;

import ru.relex.dto.LoginRequestDto;
import ru.relex.dto.UserRegistrationDto;

public interface UserService {
    String addUser(UserRegistrationDto userDto);
    String getJwtToken(LoginRequestDto loginRequestDto);
}
