package com.example.imageapi.client.responses;

import lombok.Data;

import java.util.List;

@Data
public class ImaggaResult {
    private List<ImaggaTag> tags;
}
