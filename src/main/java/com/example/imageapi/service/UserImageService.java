package com.example.imageapi.service;

import com.example.imageapi.client.responses.ImaggaTagsResponse;
import com.example.imageapi.persistence.entity.UserImage;
import com.example.imageapi.persistence.entity.UserImageObject;
import com.example.imageapi.persistence.repository.UserImageRepository;
import com.github.javafaker.Faker;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserImageService {

  private static final String SUCCESS = "success";

  private final UserImageRepository userImageRepository;

  private final ImaggaService imaggaService;

  public UserImageService(UserImageRepository userImageRepository, ImaggaService imaggaService) {
    this.userImageRepository = userImageRepository;
    this.imaggaService = imaggaService;
  }

  public List<UserImage> findImages() {
    return userImageRepository.findAll();
  }

  public List<UserImage> findImagesWithObjects(List<String> objects) {
    return userImageRepository.existsUserImageObjectsInCustomQuery(objects);
  }

  public Optional<UserImage> findImage(final Long id) {
    return userImageRepository.findById(id);
  }

  @Transactional
  public UserImage insertImage(String label, final String imageUrl, boolean detectObjects) {
    UserImage userImage = new UserImage();
    userImage.setImageUrl(imageUrl);
    String imageLabel = StringUtils.isNotEmpty(label) ? label : generateLabel();
    userImage.setLabel(imageLabel);

    if (detectObjects) {
      List<UserImageObject> userImageObjects = addImageObjects(userImage);
      userImage.setUserImageObjects(userImageObjects);
    }

    userImageRepository.save(userImage);

    return userImage;
  }

  private List<UserImageObject> addImageObjects(UserImage userImage) {
    final Optional<ImaggaTagsResponse> response = imaggaService.getTags(userImage.getImageUrl());

    final List<UserImageObject> userImageObjects = new ArrayList<>();

    if (response.isPresent()
        && SUCCESS.equals(response.get().getStatus().getType())
        && CollectionUtils.isNotEmpty(response.get().getResult().getTags())) {
      response
          .get()
          .getResult()
          .getTags()
          .forEach(
              tag -> {
                UserImageObject userImageObject = new UserImageObject();
                userImageObject.setUserImage(userImage);
                userImageObject.setObject(tag.getTagValue().getValue());
                userImageObjects.add(userImageObject);
              });
    }
    return userImageObjects;
  }

  private String generateLabel() {
    Faker faker = new Faker();
    return faker.funnyName().name();
  }
}
