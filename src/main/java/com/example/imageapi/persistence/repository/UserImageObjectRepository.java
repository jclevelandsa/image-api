package com.example.imageapi.persistence.repository;

import com.example.imageapi.persistence.entity.UserImageObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageObjectRepository extends JpaRepository<UserImageObject, Long> {
}
