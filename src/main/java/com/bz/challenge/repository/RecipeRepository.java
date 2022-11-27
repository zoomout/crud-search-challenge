package com.bz.challenge.repository;

import com.bz.challenge.repository.entity.Recipe;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    List<Recipe> findByInstructionsIgnoreCaseContaining(@Param("instructions") String instructions);

}
