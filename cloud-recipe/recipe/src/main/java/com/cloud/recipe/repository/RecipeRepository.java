package com.cloud.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloud.recipe.entity.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>{

}
