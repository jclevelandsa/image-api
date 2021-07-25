package com.example.imageapi.service;

import com.example.imageapi.client.responses.*;
import com.example.imageapi.persistence.entity.UserImage;
import com.example.imageapi.persistence.repository.UserImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserImageServiceTest {

  private static final Long ID = Long.valueOf("1");
  private static final String IMAGE_URL = "someUrl";
  private static final String LABEL = "someLabel";
  private static final String OBJECT = "wolf";

  @Mock
  private UserImageRepository userImageRepository;

  @Mock
  private ImaggaService imaggaService;

  @InjectMocks
  private UserImageService service;

  @Test
  void findImages() {
    // given
    UserImage userImage = new UserImage();
    List<UserImage> expectedUserImages = List.of(userImage);

    when(userImageRepository.findAll()).thenReturn(expectedUserImages);

    // when
    List<UserImage> actualUserImages = service.findImages();

    // then
    assertEquals(1, actualUserImages.size());
  }

  @Test
  void findImagesWithObjects() {
    // given
    UserImage userImage = new UserImage();
    List<UserImage> expectedUserImages = List.of(userImage);
    List<String> objects = List.of(OBJECT);

    when(userImageRepository.existsUserImageObjectsInCustomQuery(objects))
        .thenReturn(expectedUserImages);

    // when
    List<UserImage> actualUserImages = service.findImagesWithObjects(objects);

    // then
    assertEquals(1, actualUserImages.size());
  }

  @Test
  void findImage() {
    // given
    UserImage userImage = new UserImage();
    userImage.setId(ID);

    when(userImageRepository.findById(ID)).thenReturn(Optional.of(userImage));

    // when
    Optional<UserImage> optionalUserImage = service.findImage(ID);

    // then
    assertTrue(optionalUserImage.isPresent());
    assertEquals(ID, optionalUserImage.get().getId());
  }

  @Test
  void insertImage_detectObjectsTrue() {
    // given
    ImaggaStatus status = new ImaggaStatus();
    status.setType("success");
    ImaggaResult result = new ImaggaResult();
    ImaggaTag tag = new ImaggaTag();
    ImaggaTagValue value = new ImaggaTagValue();
    value.setValue(OBJECT);
    tag.setTagValue(value);
    result.setTags(List.of(tag));
    ImaggaTagsResponse response = new ImaggaTagsResponse(result, status);

    when(imaggaService.getTags(IMAGE_URL)).thenReturn(Optional.of(response));

    // when
    UserImage insertedUserImage = service.insertImage(LABEL, IMAGE_URL, true);

    // then
    assertNotNull(insertedUserImage);
    assertEquals(insertedUserImage.getImageUrl(), insertedUserImage.getImageUrl());
    assertEquals(insertedUserImage.getLabel(), insertedUserImage.getLabel());
    assertTrue(insertedUserImage.getUserImageObjects().size() > 0);
  }

  @Test
  void insertImage_detectObjectsFalse() {
    // when
    UserImage insertedUserImage = service.insertImage(LABEL, IMAGE_URL, false);

    // then
    assertNotNull(insertedUserImage);
    assertEquals(insertedUserImage.getImageUrl(), insertedUserImage.getImageUrl());
    assertEquals(insertedUserImage.getLabel(), insertedUserImage.getLabel());
  }

  @Test
  void insertImage_noLabel() {
    // when
    UserImage insertedUserImage = service.insertImage(null, IMAGE_URL, false);

    // then
    assertNotNull(insertedUserImage);
    assertEquals(IMAGE_URL, insertedUserImage.getImageUrl());
    assertNotNull(insertedUserImage.getLabel());
  }
}
