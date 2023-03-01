package ru.relex.exception;

public class TokenIsNotValidException extends RuntimeException {

    public TokenIsNotValidException(String message) {
        super(message);
    }
}
