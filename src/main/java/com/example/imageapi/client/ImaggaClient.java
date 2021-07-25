package com.example.imageapi.client;

import com.example.imageapi.client.responses.ImaggaTagsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${client.name.immagga}", url = "${client.endpoint.imagga}")
public interface ImaggaClient {

  @GetMapping(value = "tags")
  ImaggaTagsResponse getTags(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
      @RequestParam(value = "image_url") String imageUrl);
}
