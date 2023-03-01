package ru.relex.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.relex.config.CustomHttpHeader;
import ru.relex.dto.DateIntervalDto;
import ru.relex.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin", produces = {"application/json", "application/xml"})
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/count-operations")
    public ResponseEntity<Map<String, Long>> countOperations(@Valid @RequestBody DateIntervalDto dateIntervalDto)
            throws ParseException {
        HttpHeaders headers = CustomHttpHeader.getHttpHeader();

        return new ResponseEntity<>(
                adminService.countOperations(dateIntervalDto),
                headers,
                HttpStatus.OK
        );
    }
}
