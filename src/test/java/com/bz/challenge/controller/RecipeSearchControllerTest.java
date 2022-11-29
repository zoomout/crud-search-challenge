package com.bz.challenge.controller;

import static com.bz.challenge.utils.TestUtils.asSearchResponse;
import static com.bz.challenge.utils.TestUtils.getJsonNode;
import static com.bz.challenge.utils.TestUtils.toJson;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class RecipeSearchControllerTest extends BaseControllerTest {

    public static Stream<Arguments> equalAndNotEqualData() {
        return Stream.of(
            Arguments.of("eq"),
            Arguments.of("ne")
        );
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("equalAndNotEqualData")
    void testSearchRecipesVegetarian(String queryOperation) {
        boolean isVegetarian = queryOperation.equals("eq");
        var vegetarianDish = buildPostRequestPayload(true, 4, "apple,carrot", "slice it");
        var nonVegetarianDish = buildPostRequestPayload(false, 4, "apple,chicken", "slice it, cooke it");
        createRecipe(vegetarianDish);
        createRecipe(nonVegetarianDish);
        var response = this.mockMvc.perform(get("/api/recipes/search?query=vegetarian!" + queryOperation + "!true")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("search-recipes-vegetarian"))
            .andReturn();
        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(asSearchResponse(isVegetarian ? vegetarianDish : nonVegetarianDish));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("equalAndNotEqualData")
    void testSearchRecipesServingsNumber(String queryOperation) {
        boolean isExpectedNumber = queryOperation.equals("eq");
        var servingsExpected = buildPostRequestPayload(false, 4, "apple,carrot", "slice it");
        var servingsNotExpected = buildPostRequestPayload(false, 5, "apple,chicken", "slice it, cooke it");
        createRecipe(servingsExpected);
        createRecipe(servingsNotExpected);
        var response = this.mockMvc.perform(get("/api/recipes/search?query=servingsNumber!" + queryOperation + "!4")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("search-recipes-servingsNumber"))
            .andReturn();
        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(asSearchResponse(isExpectedNumber ? servingsExpected : servingsNotExpected));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @Test
    void testSearchIngredientsContains() {
        var appleCarrot = buildPostRequestPayload(true, 3, "apple,carrot", "slice it");
        var appleChicken = buildPostRequestPayload(false, 4, "apple,chicken", "slice it, cooke it");
        var chickenEgg = buildPostRequestPayload(false, 5, "chicken,egg", "slice it, boil it");
        createRecipe(appleCarrot);
        createRecipe(appleChicken);
        createRecipe(chickenEgg);
        var response = this.mockMvc.perform(get("/api/recipes/search?query=ingredients!cn!apple")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("search-recipes-ingredients"))
            .andReturn();
        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(asSearchResponse(appleCarrot, appleChicken));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @Test
    void testSearchIngredientsContainsMultiple() {
        var appleCarrot = buildPostRequestPayload(true, 3, "apple,carrot", "slice it");
        var appleChicken = buildPostRequestPayload(false, 4, "apple,chicken", "slice it, cooke it");
        var chickenEgg = buildPostRequestPayload(false, 5, "chicken,egg", "slice it, boil it");
        createRecipe(appleCarrot);
        createRecipe(appleChicken);
        createRecipe(chickenEgg);
        var response = this.mockMvc.perform(get("/api/recipes/search?query=ingredients!cn!apple_AND_ingredients!cn!carrot")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(asSearchResponse(appleCarrot));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @Test
    void testSearchIngredientsDoesNotContain() {
        var appleCarrot = buildPostRequestPayload(true, 3, "apple,carrot", "slice it");
        var appleChicken = buildPostRequestPayload(false, 4, "apple,chicken", "slice it, cooke it");
        var chickenEgg = buildPostRequestPayload(false, 5, "chicken,egg", "slice it, boil it");
        createRecipe(appleCarrot);
        createRecipe(appleChicken);
        createRecipe(chickenEgg);
        var response = this.mockMvc.perform(get("/api/recipes/search?query=ingredients!nc!carrot")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(asSearchResponse(appleChicken, chickenEgg));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @Test
    void testSearchIngredientsDoesNotContainMultiple() {
        var appleCarrot = buildPostRequestPayload(true, 3, "apple,carrot", "slice it");
        var appleChicken = buildPostRequestPayload(false, 4, "apple,chicken", "slice it, cooke it");
        var chickenEgg = buildPostRequestPayload(false, 5, "chicken,egg", "slice it, boil it");
        var meatPear = buildPostRequestPayload(false, 6, "meat,pear", "heat it");
        createRecipe(appleCarrot);
        createRecipe(appleChicken);
        createRecipe(chickenEgg);
        createRecipe(meatPear);
        var response = this.mockMvc.perform(get("/api/recipes/search?query=ingredients!nc!pear_AND_ingredients!nc!apple")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(asSearchResponse(chickenEgg));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @Test
    void testSearchInstructionsContains() {
        var sliceAndCook = buildPostRequestPayload(true, 1, "apple,carrot", "slice and cook");
        var sliceAndBoil = buildPostRequestPayload(true, 2, "apple,mango", "slice and boil");
        var defrostAndBake = buildPostRequestPayload(true, 3, "pear,carrot", "defrost and bake");
        createRecipe(sliceAndCook);
        createRecipe(sliceAndBoil);
        createRecipe(defrostAndBake);
        var response = this.mockMvc.perform(get("/api/recipes/search?query=instructions!cn!slice")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("search-recipes-instructions"))
            .andReturn();
        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(asSearchResponse(sliceAndCook, sliceAndBoil));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @Test
    void testSearchInstructionsContainsMultiple() {
        var sliceAndCook = buildPostRequestPayload(true, 1, "apple,carrot", "slice and cook");
        var sliceAndBoil = buildPostRequestPayload(true, 2, "apple,mango", "slice and boil");
        var defrostAndBake = buildPostRequestPayload(true, 3, "pear,carrot", "defrost and bake");
        createRecipe(sliceAndCook);
        createRecipe(sliceAndBoil);
        createRecipe(defrostAndBake);
        var response = this.mockMvc.perform(get("/api/recipes/search?query=instructions!cn!slice_AND_instructions!cn!boil")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(asSearchResponse(sliceAndBoil));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @Test
    void testSearchInstructionsDoesNotContain() {
        var sliceAndCook = buildPostRequestPayload(true, 1, "apple,carrot", "slice and cook");
        var sliceAndBoil = buildPostRequestPayload(true, 2, "apple,mango", "slice and boil");
        var defrostAndBake = buildPostRequestPayload(true, 3, "pear,carrot", "defrost and bake");
        createRecipe(sliceAndCook);
        createRecipe(sliceAndBoil);
        createRecipe(defrostAndBake);
        var response = this.mockMvc.perform(get("/api/recipes/search?query=instructions!nc!slice")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(asSearchResponse(defrostAndBake));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @Test
    void testSearchInstructionsDoesNotContainMultiple() {
        var sliceAndCook = buildPostRequestPayload(true, 1, "apple,carrot", "slice and cook");
        var sliceAndBoil = buildPostRequestPayload(true, 2, "apple,mango", "slice and boil");
        var defrostAndBake = buildPostRequestPayload(true, 3, "pear,carrot", "defrost and bake");
        createRecipe(sliceAndCook);
        createRecipe(sliceAndBoil);
        createRecipe(defrostAndBake);
        var response = this.mockMvc.perform(get("/api/recipes/search?query=instructions!nc!cook_AND_instructions!nc!boil")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(asSearchResponse(defrostAndBake));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @Test
    void testSearchMultipleSearchKeys() {
        var mangoBanana = buildPostRequestPayload(true, 2, "mango,banana", "slice it");
        var appleCarrot = buildPostRequestPayload(true, 3, "apple,carrot", "slice it");
        var appleChicken = buildPostRequestPayload(false, 4, "apple,chicken", "slice it, cooke it");
        var chickenEgg = buildPostRequestPayload(false, 5, "chicken,egg", "slice it, boil it");
        var meatPear = buildPostRequestPayload(false, 6, "meat,pear", "heat it");
        createRecipe(mangoBanana);
        createRecipe(appleCarrot);
        createRecipe(appleChicken);
        createRecipe(chickenEgg);
        createRecipe(meatPear);
        var response = this.mockMvc.perform(get("/api/recipes/search?query="
                + "vegetarian!eq!false"
                + "_AND_"
                + "ingredients!nc!egg"
                + "_AND_"
                + "servingsNumber!ne!6"
            )
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(asSearchResponse(appleChicken));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @Test
    void testSearchAllMultipleSearchKeys() {
        var mangoBanana = buildPostRequestPayload(true, 2, "mango,banana", "slice it");
        var appleCarrot = buildPostRequestPayload(false, 3, "apple,carrot,lam", "slice it, bake it");
        var appleChicken = buildPostRequestPayload(false, 4, "apple,chicken", "slice it, cooke it");
        var chickenEgg = buildPostRequestPayload(false, 4, "chicken,egg", "slice it, boil it");
        var meatPear = buildPostRequestPayload(false, 6, "chicken,pear", "heat it");
        createRecipe(mangoBanana);
        createRecipe(appleCarrot);
        createRecipe(appleChicken);
        createRecipe(chickenEgg);
        createRecipe(meatPear);
        var response = this.mockMvc.perform(get("/api/recipes/search?query="
                + "vegetarian!eq!false"
                + "_AND_"
                + "ingredients!cn!chicken"
                + "_AND_"
                + "servingsNumber!eq!4"
                + "_AND_"
                + "instructions!nc!cooke it"
            )
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("search-recipes-complexQuery"))
            .andReturn();
        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(asSearchResponse(chickenEgg));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    @SneakyThrows
    @Test
    void testSearchPagination() {
        var vegetarianRecipes = new ArrayList<ObjectNode>();
        for (int i = 0; i < 20; i++) {
            var isVegetarian = i % 2 == 0;
            var payload = buildPostRequestPayload(isVegetarian, 2, "products", "bake it");
            createRecipe(payload);
            if (isVegetarian) {
                vegetarianRecipes.add(payload);
            }
        }
        int page = 2;
        int size = 3;
        var expectedRecipes = vegetarianRecipes.stream().skip(size * page).limit(size).toList();
        var response = this.mockMvc.perform(get("/api/recipes/search?query=vegetarian!eq!true&page=" + page + "&size=" + size)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(asSearchResponse(expectedRecipes.toArray(new ObjectNode[]{})));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    public static Stream<Arguments> dataInvalidQueries() {
        return Stream.of(
            Arguments.of("vegetarian!eq!", "Invalid query: Query parameters size should be equal to 3, query=vegetarian!eq!"),
            Arguments.of("invalidKey!eq!value", "Invalid query: Key is no allowed: invalidKey. Allowed keys: vegetarian,servingsNumber,ingredients,instructions")
        );
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("dataInvalidQueries")
    void testInvalidQueries(String invalidQuery, String expectedMessage) {
        var payload = buildPostRequestPayload(true, 2, "products", "bake it");
        createRecipe(payload);
        var response = this.mockMvc.perform(get("/api/recipes/search?query=" + invalidQuery)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
        var searchResponse = response.getResponse().getContentAsString();
        var expected = toJson(getJsonNode().put("message", expectedMessage));
        JSONAssert.assertEquals(expected, searchResponse, JSONCompareMode.LENIENT);
    }

    private ResultActions createRecipe(ObjectNode payload) throws Exception {
        return this.mockMvc.perform(post("/api/recipes")
                .content(toJson(payload))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    private static ObjectNode buildPostRequestPayload(
        boolean vegetarian,
        int servingsNumber,
        String ingredients,
        String instructions
    ) {
        return getJsonNode()
            .put("name", "dish-" + (vegetarian ? "veggie-" : "non-veggie-") + servingsNumber + "-" + ingredients)
            .put("vegetarian", vegetarian)
            .put("servingsNumber", servingsNumber)
            .put("ingredients", ingredients)
            .put("instructions", instructions);
    }

}
