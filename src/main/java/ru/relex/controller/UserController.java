package ru.relex.controller;

import org.springframework.http.HttpHeaders;
import ru.relex.config.CustomHttpHeader;
import ru.relex.dto.LoginRequestDto;
import ru.relex.dto.UserRegistrationDto;
import ru.relex.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = {"application/json", "application/xml"})
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody UserRegistrationDto userModel) {
        String username = userService.addUser(userModel);

        HttpHeaders headers = CustomHttpHeader.getHttpHeader();
        return new ResponseEntity(
                "User \"" + username + "\" is REGISTERED. Do login to get a secret key to be authenticated",
                headers,
                HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        String jwt = userService.getJwtToken(loginRequestDto);

        HttpHeaders headers = CustomHttpHeader.getHttpHeader();
        return new ResponseEntity<>(
                Map.of("jwt_token", jwt),
                headers,
                HttpStatus.OK
        );
    }
}
