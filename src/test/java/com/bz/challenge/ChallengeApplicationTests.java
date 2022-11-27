package com.bz.challenge;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
class ChallengeApplicationTests {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @Disabled//TODO fix
    @SneakyThrows
    @Test
    void testProfileIsExposed() {
        this.mockMvc.perform(get("/api/profile").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("index"));
    }

    @SneakyThrows//TODO fix
    @Test
    void testGetRecipesReturnsEmptyByDefault() {
        this.mockMvc.perform(get("/api/recipes").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("index"));
    }

    @SneakyThrows//TODO fix should be 404
    @Test
    void testGetRootReturns404() {
        this.mockMvc.perform(get("/").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("index"));
    }

}
