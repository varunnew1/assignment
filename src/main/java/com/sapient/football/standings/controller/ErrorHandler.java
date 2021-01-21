package com.sapient.football.standings.controller;

import com.sapient.football.standings.exceptions.ValidationException;
import com.sapient.football.standings.models.response.ApiResponse;
import com.sapient.football.standings.models.response.FieldError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    public ApiResponse handleValidationException(ValidationException ex, WebRequest request) {
        ApiResponse apiResponse = new ApiResponse(false, null, ex.getCode(),
                ex.getMessage(), ex.getErrors()!=null?getFieldErrors(ex.getErrors().getFieldErrors()):null);
        return apiResponse;
    }

    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse handleRuntimeException(RuntimeException ex, WebRequest request) {
        ApiResponse apiResponse = new ApiResponse(false, null, null,
                ex.getMessage(), null);
        return apiResponse;
    }

    protected List<FieldError> getFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        if (fieldErrors == null) return Collections.emptyList();
        List<FieldError> result = new ArrayList<>(fieldErrors.size());
        fieldErrors.forEach(fe -> result.add(new FieldError(fe.getField(), fe.getDefaultMessage())));
        return result;
    }
}
