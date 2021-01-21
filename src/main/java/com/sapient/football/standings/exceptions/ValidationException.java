package com.sapient.football.standings.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.Errors;

@Getter
@Setter
@ToString
public class ValidationException extends RuntimeException {
    private int code;
    private Errors errors;

    public ValidationException(String message) {
        super(message);
    }
}
