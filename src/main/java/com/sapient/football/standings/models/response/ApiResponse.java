package com.sapient.football.standings.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("field_errors")
    private List<FieldError> fieldErrors = new ArrayList<>(4);


    public void addFieldError(String field, String message) {
        this.fieldErrors.add(new FieldError(field, message));
    }

    public void addFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors.addAll(fieldErrors);
    }

}
