package com.example.imageapi.client.responses;

import lombok.Data;
import lombok.NonNull;

@Data
public class ImaggaTagsResponse {
  @NonNull
  private ImaggaResult result;

  @NonNull
  private ImaggaStatus status;
}
