package com.example.imageapi.persistence.repository;

import com.example.imageapi.persistence.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

  @Query(
      "select ui from UserImage ui where exists (select uio from UserImageObject uio "
          + "where uio.userImage = ui and uio.object in (:objects))")
  List<UserImage> existsUserImageObjectsInCustomQuery(@Param("objects") List<String> objects);
}
