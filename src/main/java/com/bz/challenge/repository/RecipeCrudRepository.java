package com.bz.challenge.repository;

import com.bz.challenge.repository.entity.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * CRUD operation repository for {@link com.bz.challenge.repository.entity.Recipe}.
 * <p>
 * This repository is used for generation of Spring Data REST CRUD services via {@link org.springframework.data.rest.core.annotation.RepositoryRestResource}
 */
@RepositoryRestResource
public interface RecipeCrudRepository extends PagingAndSortingRepository<Recipe, Long>, CrudRepository<Recipe, Long> {

}