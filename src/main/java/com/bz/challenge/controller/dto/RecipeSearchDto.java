package com.bz.challenge.controller.dto;

import com.bz.challenge.service.search.SearchCriterion;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSearchDto {

    private List<SearchCriterion> searchCriterionList;

}
