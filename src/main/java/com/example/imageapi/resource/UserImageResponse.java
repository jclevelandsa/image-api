package com.example.imageapi.resource;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserImageResponse {
  private Long id;
  private String imageUrl;
  private String label;
  private List<UserImageObjectResponse> userImageObjects = new ArrayList<>();
}
