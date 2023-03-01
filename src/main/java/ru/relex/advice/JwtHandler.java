package ru.relex.advice;

import ru.relex.exception.JwtExpiredException;
import ru.relex.exception.TokenIsNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class JwtHandler {

    @ExceptionHandler(TokenIsNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleTokenValidation(TokenIsNotValidException e) {
        return e.getMessage();
    }

    @ExceptionHandler(JwtExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleJwtExpiration(JwtExpiredException e) {
        return e.getMessage();
    }
}
