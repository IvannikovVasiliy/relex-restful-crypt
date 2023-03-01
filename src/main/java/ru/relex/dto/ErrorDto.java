package ru.relex.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ErrorDto {
    private final String field;
    private final String message;
}
