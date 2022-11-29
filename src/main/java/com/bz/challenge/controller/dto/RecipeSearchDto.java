package com.bz.challenge.controller.dto;

import com.bz.challenge.service.search.SearchCriterion;
import java.util.List;

public record RecipeSearchDto(List<SearchCriterion> searchCriterionList) {

}
