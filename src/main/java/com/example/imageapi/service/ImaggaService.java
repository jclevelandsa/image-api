package com.example.imageapi.service;

import com.example.imageapi.client.ImaggaClient;
import com.example.imageapi.client.responses.ImaggaTagsResponse;
import com.example.imageapi.error.ApiErrorException;
import feign.FeignException;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
public class ImaggaService {

  private static final String API_KEY = "IMAGGA_APIKEY";

  private static final String API_SECRET = "IMAGGA_APISECRET";

  private final ImaggaClient imaggaClient;

  private final CircuitBreaker tagsCircuitBreaker;

  private final String credentials;

  public ImaggaService(
      ImaggaClient imaggaClient, CircuitBreakerFactory circuitBreakerFactory, Environment env) {
    this.imaggaClient = imaggaClient;
    this.tagsCircuitBreaker = circuitBreakerFactory.create("tags");
    credentials = env.getProperty(API_KEY) + ":" + env.getProperty(API_SECRET);
  }

  public Optional<ImaggaTagsResponse> getTags(String imageUrl) {
    final String authorizationHeader =
        "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

    return tagsCircuitBreaker.run(
        () -> Optional.of(imaggaClient.getTags(authorizationHeader, imageUrl)),
        throwable -> {
          if (throwable instanceof FeignException
              && ((FeignException) throwable).status() == HttpStatus.BAD_REQUEST.value()) {
            throw new ApiErrorException(
                String.format("An error occurred trying to get image tags. Url: %s", imageUrl),
                throwable);
          }
          throw new RuntimeException(throwable);
        });
  }
}
