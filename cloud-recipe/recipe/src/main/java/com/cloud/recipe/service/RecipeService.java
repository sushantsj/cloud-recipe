package com.cloud.recipe.service;

import java.util.List;
import java.util.Map;

import com.cloud.recipe.entity.Recipe;
import com.cloud.recipe.entity.RecipeWrapper;
import com.cloud.recipe.exceptions.RecipeException;
import com.cloud.recipe.exceptions.UserException;

public interface RecipeService {
	
	Recipe createRecipe(RecipeWrapper recipe) throws RecipeException;
	
	List<Recipe> getAllRecipeByQuery(Map<String, String> query) throws RecipeException;
	
	Recipe updateRecipe(RecipeWrapper recipe) throws RecipeException, UserException;
	
	Boolean deleteRecipe(Long id, String username, String password) throws RecipeException, UserException;

	List<Recipe> getAllRecipeByUser(String username, String password) throws RecipeException, UserException;
}
