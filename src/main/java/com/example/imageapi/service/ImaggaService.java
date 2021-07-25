package com.example.imageapi.service;

import com.example.imageapi.client.ImaggaClient;
import com.example.imageapi.client.responses.ImaggaTagsResponse;
import com.example.imageapi.error.ApiErrorException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
public class ImaggaService {

  private final ImaggaClient imaggaClient;

  private final CircuitBreaker tagsCircuitBreaker;

  @Value("${client.apikey.imagga}")
  private String apiKey;

  @Value("${client.apisecret.imagga}")
  private String apiSecret;

  public ImaggaService(ImaggaClient imaggaClient, CircuitBreakerFactory circuitBreakerFactory) {
    this.imaggaClient = imaggaClient;
    this.tagsCircuitBreaker = circuitBreakerFactory.create("tags");
  }

  public Optional<ImaggaTagsResponse> getTags(String imageUrl) {
    final String credentialsToEncode = apiKey + ":" + apiSecret;
    final String authorizationHeader =
        "Basic "
            + Base64.getEncoder()
                .encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

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
