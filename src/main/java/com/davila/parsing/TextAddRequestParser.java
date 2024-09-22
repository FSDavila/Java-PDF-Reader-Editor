package com.davila.parsing;

import java.io.IOException;

import com.davila.model.TextAddRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TextAddRequestParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static TextAddRequest fromJson(String json) throws IOException {
        return objectMapper.readValue(json, TextAddRequest.class);
    }

    public static String toJson(TextAddRequest request) throws IOException {
        return objectMapper.writeValueAsString(request);
    }
}