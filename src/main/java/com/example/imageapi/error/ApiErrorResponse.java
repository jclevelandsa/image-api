package com.example.imageapi.error;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApiErrorResponse {
  private List<String> errors;

  public ApiErrorResponse() {}

  public ApiErrorResponse(List<String> errors) {
    this.errors = errors;
  }

  public ApiErrorResponse(String error) {
    this(Collections.singletonList(error));
  }

  public ApiErrorResponse(String... errors) {
    this(Arrays.asList(errors));
  }

  public List<String> getErrors() {
    return errors;
  }

  public void setErrors(List<String> errors) {
    this.errors = errors;
  }
}
