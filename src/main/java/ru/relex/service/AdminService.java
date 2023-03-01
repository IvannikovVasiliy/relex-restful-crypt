package ru.relex.service;

import ru.relex.dto.DateIntervalDto;

import java.text.ParseException;
import java.util.Map;

public interface AdminService {
    Map<String, Long> countOperations(DateIntervalDto intervalDto) throws ParseException;
}
