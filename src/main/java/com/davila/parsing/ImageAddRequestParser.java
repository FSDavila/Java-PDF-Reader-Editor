package com.davila.parsing;

import com.davila.model.ImageAddRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class ImageAddRequestParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ImageAddRequest fromJson(String json) throws IOException {
        return objectMapper.readValue(json, ImageAddRequest.class);
    }

    public static String toJson(ImageAddRequest request) throws IOException {
        return objectMapper.writeValueAsString(request);
    }
}