package com.example.imageapi.client.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImaggaTagValue {
  @JsonProperty("en")
  private String value;
}
