package com.example.imageapi.resource;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class UserImageRequest {

    @NotNull
    @Length(max = 2048)
    private String imageUrl;

    @Length(max = 200)
    private String label;

    private boolean detectObjects;
}
