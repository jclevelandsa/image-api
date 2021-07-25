package com.example.imageapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceNotFoundException extends ResponseStatusException {
  public ResourceNotFoundException(HttpStatus status) {
    super(status);
  }

  public ResourceNotFoundException(HttpStatus status, String reason) {
    super(status, reason);
  }

  public ResourceNotFoundException(HttpStatus status, String reason, Throwable cause) {
    super(status, reason, cause);
  }

  public ResourceNotFoundException(int rawStatusCode, String reason, Throwable cause) {
    super(rawStatusCode, reason, cause);
  }
}
