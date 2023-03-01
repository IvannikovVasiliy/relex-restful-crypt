package ru.relex.advice;

import ru.relex.dto.ErrorDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.relex.exception.AccessDeniedException;
import ru.relex.exception.DateTimeFormatException;
import ru.relex.exception.ResourceAlreadyExistsException;
import ru.relex.exception.ResourceNotFoundException;

import java.util.List;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String handleAccessDenied(AccessDeniedException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleResourceExists(ResourceAlreadyExistsException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleResourceNotFound(ResourceNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(DateTimeFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleDateTimeFormat(DateTimeFormatException e) {
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ErrorDto> handleMethodArg(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new ErrorDto(error.getField(), error.getDefaultMessage()))
                .toList();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ErrorDto> handleConstraintViolation(ConstraintViolationException e) {
        return e.getConstraintViolations()
                .stream()
                .map(error -> new ErrorDto(error.getPropertyPath().toString(), error.getMessage()))
                .toList();
    }
}
