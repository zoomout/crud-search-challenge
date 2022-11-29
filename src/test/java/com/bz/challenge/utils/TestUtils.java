package com.bz.challenge.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.Arrays;

public class TestUtils {

    private static final ObjectMapper OBJECT_MAPPER = getObjectMapper();

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public static String toJson(Object obj) throws IOException {
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    public static ObjectNode getJsonNode() {
        return OBJECT_MAPPER.createObjectNode();
    }

    public static ArrayNode getArrayJsonNode() {
        return OBJECT_MAPPER.createArrayNode();
    }

    public static ObjectNode asFindAllResponse(ObjectNode... payload) {
        return getJsonNode().set("_embedded", getJsonNode().set("recipes", getArrayJsonNode().addAll(Arrays.stream(payload).toList())));
    }

    public static ObjectNode asSearchResponse(ObjectNode... payload) {
        return getJsonNode().set("content", getArrayJsonNode().addAll(Arrays.stream(payload).toList()));
    }

    public static ObjectNode buildPostRequestPayload() {
        return getJsonNode()
            .put("name", "dish")
            .put("vegetarian", false)
            .put("servingsNumber", 4)
            .put("ingredients", "carrot, apple, meat")
            .put("instructions", "Grind the carrots and slice the apples, add some meat");
    }

}
