package com.cloud.recipe.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.recipe.entity.Recipe;
import com.cloud.recipe.entity.RecipeWrapper;
import com.cloud.recipe.exceptions.RecipeException;
import com.cloud.recipe.exceptions.UserException;
import com.cloud.recipe.service.RecipeService;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

	private final Logger log = LoggerFactory.getLogger(RecipeController.class);

	private RecipeService recipeService;

	@PostMapping("/create-recipe")
	public ResponseEntity<Recipe> createRecipe(@RequestBody RecipeWrapper recipeWrapper) throws RecipeException {
		log.info("Rest request to create recipe");
		Recipe recipe = null;
		try {
			if (recipeWrapper.getUsername() != null && recipeWrapper.getTitle() != null
					&& recipeWrapper.getBody() != null && recipeWrapper.getEstimatedTime() != null
					&& recipeWrapper.getPassword() != null) {
				recipe = recipeService.createRecipe(recipeWrapper);
			} else {
				throw new RecipeException("Parameters missing, Fill the missing parameters");
			}
		} catch (RecipeException e) {
			throw new RecipeException(e.getMessage());
		}
		return new ResponseEntity<Recipe>(recipe, HttpStatus.CREATED);
	}

	@GetMapping("/getall-recipes/{recipe}")
	public ResponseEntity<List<Recipe>> getAllRecipeByQuery(@PathVariable("recipe") String query)
			throws RecipeException {
		log.info("Rest request to get recipes by query");
		List<Recipe> recipeList = null;
		try {
			if (!query.isEmpty()) {
				Map<String, String> searchQuery = new HashMap<>();
				searchQuery.put("title", query);
				recipeList = recipeService.getAllRecipeByQuery(searchQuery);
			} else {
				throw new RecipeException("Recipe not found");
			}
		} catch (RecipeException e) {
			throw new RecipeException(e.getMessage());
		}
		return new ResponseEntity<List<Recipe>>(recipeList, HttpStatus.OK);
	}

	@GetMapping("/getallbyuser/{username}/{password}")
	public ResponseEntity<List<Recipe>> getAllRecipeByUser(@PathVariable("username") String username,
			@PathVariable("password") String password) throws RecipeException, UserException {
		log.info("Rest request to get recipes by user");
		List<Recipe> recipeList = null;
		try {
			if (username != null && password != null) {
				recipeList = recipeService.getAllRecipeByUser(username, password);
			} else {
				throw new UserException("Username or Password Invalid or doesn't exist");
			}
		} catch (RecipeException e) {
			throw new RecipeException(e.getMessage());
		} catch (UserException e) {
			throw new UserException(e.getMessage());
		}
		return new ResponseEntity<List<Recipe>>(recipeList, HttpStatus.OK);
	}

	@PutMapping("/update-recipe")
	public ResponseEntity<Recipe> updateRecipe(@RequestBody RecipeWrapper recipeWrapper)
			throws RecipeException, UserException {
		log.info("Rest request to update the recipe");
		Recipe recipe = null;
		try {
			if (recipeWrapper.getUsername() != null && recipeWrapper.getTitle() != null
					&& recipeWrapper.getBody() != null && recipeWrapper.getEstimatedTime() != null
					&& recipeWrapper.getPassword() != null) {
				recipe = recipeService.updateRecipe(recipeWrapper);
			} else {
				throw new RecipeException("Parameters missing, Fill the missing parameters");
			}
		} catch (RecipeException e) {
			throw new RecipeException(e.getMessage());
		} catch (UserException e) {
			throw new UserException(e.getMessage());
		}
		return new ResponseEntity<Recipe>(recipe, HttpStatus.OK);
	}

	@DeleteMapping("delete-recipe/{id}/{username}/{password}")
	public ResponseEntity<Boolean> deleteRecipe(@PathVariable("id") Long recipeId,
			@PathVariable("username") String username, @PathVariable("password") String password)
			throws RecipeException, UserException {
		log.info("Rest request to delete recipe");
		boolean result = false;
		try {
			if (recipeId != null && username != null && password != null) {
				result = recipeService.deleteRecipe(recipeId, username, password);
			} else {
				throw new UserException("Username or passowrd incorrect");
			}
		} catch (RecipeException e) {
			throw new RecipeException(e.getMessage());
		} catch (UserException e) {
			throw new UserException(e.getMessage());
		}
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
}
