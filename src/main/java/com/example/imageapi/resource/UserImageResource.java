package com.example.imageapi.resource;

import com.example.imageapi.error.ResourceNotFoundException;
import com.example.imageapi.persistence.entity.UserImage;
import com.example.imageapi.service.UserImageService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserImageResource {

  private static final ModelMapper modelMapper = new ModelMapper();
  private final UserImageService userImageService;

  public UserImageResource(UserImageService userImageService) {
    this.userImageService = userImageService;
  }

  @GetMapping(path = "/images")
  @Operation(
      summary = "Find user images",
      description =
          "This method returns all user images with metadata or only ones which contain the detected objects in the specified query parameter ")
  public List<UserImageResponse> findImages(
      @RequestParam(name = "objects", required = false) Optional<List<String>> objects) {
    List<UserImageResponse> userImageResponses = new ArrayList<>();

    List<UserImage> userImages;
    if (objects.isPresent()) {
      userImages = userImageService.findImagesWithObjects(objects.get());
    } else {
      userImages = userImageService.findImages();
    }
    mapUserImages(userImageResponses, userImages);
    return userImageResponses;
  }

  private void mapUserImages(
      List<UserImageResponse> userImageResponses, List<UserImage> userImages) {
    userImages.forEach(
        i -> {
          UserImageResponse userImageResponse = modelMapper.map(i, UserImageResponse.class);
          userImageResponses.add(userImageResponse);
        });
  }

  @GetMapping(path = "/images/{id}")
  @Operation(
      summary = "Find user image by id",
      description = "This method returns the user image for the given id, 404 if does not exist")
  public UserImageResponse findImage(@PathVariable Long id) {
    Optional<UserImage> userImageOptional = userImageService.findImage(id);

    return userImageOptional
        .map(i -> modelMapper.map(i, UserImageResponse.class))
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    HttpStatus.NOT_FOUND, String.format("Image not found with id: %s", id)));
  }

  @PostMapping(path = "/images")
  @ResponseBody
  @Operation(summary = "Add a user image", description = "This method adds a user image")
  public UserImageResponse addImage(@Valid @RequestBody UserImageRequest userImageRequest) {
    UserImage userImage =
        userImageService.insertImage(
            userImageRequest.getLabel(),
            userImageRequest.getImageUrl(),
            userImageRequest.isDetectObjects());

    return modelMapper.map(userImage, UserImageResponse.class);
  }
}
