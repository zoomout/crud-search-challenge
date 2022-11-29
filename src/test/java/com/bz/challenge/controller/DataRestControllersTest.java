package com.bz.challenge.controller;

import static com.bz.challenge.utils.TestUtils.asFindAllResponse;
import static com.bz.challenge.utils.TestUtils.buildPostRequestPayload;
import static com.bz.challenge.utils.TestUtils.toJson;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.MediaType;

class DataRestControllersTest extends BaseControllerTest {

    @SneakyThrows
    @Test
    void testProfileIsExposed() {
        this.mockMvc.perform(get("/api/profile").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("profile"));
    }

    @SneakyThrows
    @Test
    void testGetRecipesReturnsEmptyByDefault() {
        this.mockMvc.perform(get("/api/recipes").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.recipes").isEmpty())
            .andDo(document("get-recipes-empty"));
    }

    @SneakyThrows
    @Test
    void testPostRecipes() {
        var payload = toJson(buildPostRequestPayload());
        var response = this.mockMvc.perform(post("/api/recipes")
                .content(payload)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(document("post-recipes"))
            .andReturn();
        var responseString = response.getResponse().getContentAsString();
        JSONAssert.assertEquals(payload, responseString, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @Test
    void testGetRecipes() {
        var payload = buildPostRequestPayload();
        createRecipe(payload);
        var response = this.mockMvc.perform(get("/api/recipes")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("get-recipes"))
            .andReturn();
        var responseString = response.getResponse().getContentAsString();
        var embedded = asFindAllResponse(payload);
        JSONAssert.assertEquals(toJson(embedded), responseString, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @Test
    void testGetPagination() {
        var recipes = new ArrayList<ObjectNode>();
        for (int i = 0; i < 20; i++) {
            var payload = buildPostRequestPayload();
            createRecipe(payload);
            recipes.add(payload);
        }
        int page = 2;
        int size = 5;
        var response = this.mockMvc.perform(get("/api/recipes?page=" + page + "&size=" + size)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("get-recipes"))
            .andReturn();
        var expectedRecipes = recipes.stream().skip(size * page).limit(size).toList();

        var responseString = response.getResponse().getContentAsString();
        var embedded = asFindAllResponse(expectedRecipes.toArray(new ObjectNode[]{}));
        JSONAssert.assertEquals(toJson(embedded), responseString, JSONCompareMode.LENIENT);
    }

    private void createRecipe(ObjectNode payload) throws Exception {
        this.mockMvc.perform(post("/api/recipes")
                .content(toJson(payload))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    void testDeleteRecipes() {
        var payload = buildPostRequestPayload();
        createRecipe(payload);
        this.mockMvc.perform(delete("/api/recipes/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("delete-recipes"))
            .andExpect(jsonPath("$").doesNotExist());
    }

}
