package com.example.imageapi.error;

public class ApiErrorException extends RuntimeException {

  public ApiErrorException() {
    super();
  }

  public ApiErrorException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ApiErrorException(final String message) {
    super(message);
  }

  public ApiErrorException(final Throwable cause) {
    super(cause);
  }
}
