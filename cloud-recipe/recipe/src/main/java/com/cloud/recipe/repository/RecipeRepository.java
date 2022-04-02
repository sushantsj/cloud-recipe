package com.cloud.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cloud.recipe.entity.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe>{
	
	List<Recipe> findAllByUsername(String username);

}
