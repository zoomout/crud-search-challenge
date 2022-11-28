package com.bz.challenge.service.recipe;

import static com.bz.challenge.service.search.Specifications.combineAnd;

import com.bz.challenge.repository.RecipeSearchRepository;
import com.bz.challenge.repository.entity.Recipe;
import com.bz.challenge.service.search.SearchCriterion;
import com.bz.challenge.service.search.SearchSpecification;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Search operations service for {@link com.bz.challenge.repository.entity.Recipe}
 */
@Service
@RequiredArgsConstructor
public class RecipeSearchService {

    private final RecipeSearchRepository repository;

    /**
     * Search for {@link com.bz.challenge.repository.entity.Recipe} using search criteria
     *
     * @param searchCriteria search criteria list
     * @param pageable       settings for paging of the response
     * @return a {@link org.springframework.data.domain.Page} of {@link com.bz.challenge.repository.entity.Recipe}
     */
    public Page<Recipe> searchRecipes(List<SearchCriterion> searchCriteria, Pageable pageable) {
        final var recipeSpecification = buildRecipeSpecification(searchCriteria);
        return repository.findAll(recipeSpecification.orElse(null), pageable);
    }

    private Optional<Specification<Recipe>> buildRecipeSpecification(List<SearchCriterion> searchCriteria) {
        if (Objects.isNull(searchCriteria)) {
            return Optional.empty();
        }
        if (searchCriteria.isEmpty()) {
            return Optional.empty();
        }
        final var recipeSpecificationList = searchCriteria.stream().map(SearchSpecification::<Recipe>with).toList();
        return combineAnd(recipeSpecificationList);
    }

}
