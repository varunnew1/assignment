package com.sapient.football.standings.models.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FieldError {

    private String field;
    private String message;


    public FieldError(String field, String message) {
        super();
        this.field = field;
        this.message = message;
    }
}