package com.example.imageapi.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiErrorHandler extends ResponseEntityExceptionHandler {

  // 400
  @ExceptionHandler(ApiErrorException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<ApiErrorResponse> handleApiException(final ApiErrorException ex) {
    ApiErrorResponse errorResponse = new ApiErrorResponse(ex.getMessage());
    logger.error("ApiErrorHandler.handleApiException: ", ex);
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @Override
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException ex,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
    List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
    String error;
    for (FieldError fieldError : fieldErrors) {
      error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
      errors.add(error);
    }
    for (ObjectError objectError : globalErrors) {
      error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
      errors.add(error);
    }
    ApiErrorResponse errorResponse = new ApiErrorResponse(errors);
    return ResponseEntity.badRequest().body(errorResponse);
  }

  // 404
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  protected ResponseEntity<Object> handleNotFound(final ResourceNotFoundException ex) {
    ApiErrorResponse errorResponse = new ApiErrorResponse(ex.getReason());
    return ResponseEntity.badRequest().body(errorResponse);
  }

  // 500
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected ResponseEntity<Object> handleGeneralException(final Exception ex) {
    logger.error("ApiErrorHandler.handleGeneralException: ", ex);
    return ResponseEntity.internalServerError()
        .body(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
  }
}
