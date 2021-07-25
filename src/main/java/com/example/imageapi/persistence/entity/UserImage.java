package com.example.imageapi.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_image")
@Data
@EqualsAndHashCode
public class UserImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "label")
  private String label;

  @OneToMany(
      mappedBy = "userImage",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<UserImageObject> userImageObjects = new ArrayList<>();
}
