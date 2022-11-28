package com.bz.challenge.core.exception;

import com.bz.challenge.controller.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Holds handlers to customize default Spring Framework exception handling
 */
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException exception) {
        var violations = exception.getConstraintViolations().stream()
            .map(constraintViolation -> "'" + constraintViolation.getPropertyPath() + "' " + constraintViolation.getMessage())
            .collect(Collectors.joining(","));
        return new ResponseEntity<>(new ErrorResponseDto(violations), HttpStatus.BAD_REQUEST);
    }

}
