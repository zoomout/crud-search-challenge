package com.bz.challenge.repository;

import com.bz.challenge.repository.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Search operation repository for {@link com.bz.challenge.repository.entity.Recipe}
 */
@Repository
public interface RecipeSearchRepository extends JpaRepository<Recipe, Integer>, JpaSpecificationExecutor<Recipe> {

}
