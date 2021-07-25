package com.example.imageapi.client.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ImaggaTag {
    private BigDecimal confidence;
    @JsonProperty("tag")
    private ImaggaTagValue tagValue;
}
