package com.example.imageapi.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "user_image_object")
@Data
@EqualsAndHashCode
public class UserImageObject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserImage.class, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_image_id", referencedColumnName = "id", nullable = false)
  private UserImage userImage;

  @Column(name = "object")
  private String object;
}
