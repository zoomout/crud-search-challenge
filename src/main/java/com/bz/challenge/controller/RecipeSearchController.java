package com.bz.challenge.controller;

import com.bz.challenge.controller.dto.RecipeSearchDto;
import com.bz.challenge.repository.entity.Recipe;
import com.bz.challenge.service.recipe.RecipeSearchService;
import com.bz.challenge.service.search.support.SearchQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API that provides {@link com.bz.challenge.repository.entity.Recipe} search functionality
 */
@RequiredArgsConstructor
@RestController
public class RecipeSearchController {

    private final RecipeSearchService recipeSearchService;

    @GetMapping("/api/recipes/search")
    public Page<Recipe> searchByCriteria(
        @SearchQuery(
            value = "query",
            allowedKeys = {"vegetarian", "servingsNumber", "ingredients", "instructions"}
        ) RecipeSearchDto recipeSearchDto,
        Pageable pageable
    ) {
        return recipeSearchService.searchRecipes(recipeSearchDto.getSearchCriterionList(), pageable);
    }
}
