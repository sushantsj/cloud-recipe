package com.cloud.recipe.serviceimpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cloud.recipe.entity.Recipe;
import com.cloud.recipe.entity.RecipeWrapper;
import com.cloud.recipe.entity.User;
import com.cloud.recipe.exceptions.RecipeException;
import com.cloud.recipe.exceptions.UserException;
import com.cloud.recipe.repository.RecipeRepository;
import com.cloud.recipe.repository.UserRepository;
import com.cloud.recipe.service.RecipeService;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class RecipeServiceImpl implements RecipeService {

	private final Logger log = LoggerFactory.getLogger(RecipeService.class);

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Bean
	public TimedAspect timedAspect(MeterRegistry registry) {
	    return new TimedAspect(registry);
	}

	@Override
	@Timed(value = "recipe.time", description = "Time taken to create recipe")
	public Recipe createRecipe(RecipeWrapper recipe) throws RecipeException {
		log.info("Class: RecipeServiceImpl and method: createRecipe");
		
		User checkUser = userRepository.findByUsernameAndPassword(recipe.getUsername(), recipe.getPassword());
		Recipe newRecipe = new Recipe();
		try {
			if (checkUser != null) {
				if (!recipe.getTitle().isEmpty() && !recipe.getPassword().isEmpty() && !recipe.getBody().isEmpty()) {
					newRecipe.setBody(recipe.getBody());
					newRecipe.setTitle(recipe.getTitle());
					newRecipe.setEstimatedTime(recipe.getEstimatedTime());
					newRecipe.setUsername(recipe.getUsername());
					newRecipe.setDate(Instant.now());
					recipeRepository.save(newRecipe);
				}
			} else {
				throw new RecipeException("Invalid Username or Password");
			}
		} catch (RecipeException e) {
			throw new RecipeException(e.getMessage());
		}

		return newRecipe;
	}

	@Override
	public List<Recipe> getAllRecipeByQuery(Map<String, String> recipeSearch) throws RecipeException {
		log.info("Class: RecipeServiceImpl and method: getAllRecipeByQuery");
		List<Recipe> listAllRecipe = new ArrayList<Recipe>();
		listAllRecipe = recipeRepository.findAll(new Specification<Recipe>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				for (Map.Entry<String, String> entry : recipeSearch.entrySet()) {
					if (!entry.getValue().equals("") && entry.getValue() != null
							&& !entry.getKey().equalsIgnoreCase("title")) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.like(root.get(entry.getKey()), "%" + entry.getValue() + "%")));
					}
					if (!entry.getValue().equals("") && entry.getKey().equalsIgnoreCase("title")
							&& entry.getValue() != null) {
						List<String> recipes = Arrays.asList(entry.getValue().split(","));
						for (String recipe : recipes) {
							predicates.add(criteriaBuilder
									.and(criteriaBuilder.like(root.get(entry.getKey()), "%" + recipe + "%")));
						}
					}
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
		return listAllRecipe;
	}

	@Override
	public Recipe updateRecipe(RecipeWrapper recipe) throws RecipeException, UserException {
		log.info("Class: RecipeServiceImpl and method: updateRecipe");
		User checkUser = userRepository.findByUsernameAndPassword(recipe.getUsername(), recipe.getPassword());
		Optional<Recipe> newRecipe = recipeRepository.findById(recipe.getRecipeId());
		try {
			if (checkUser != null) {
				if (!newRecipe.isEmpty()) {
					if (!recipe.getTitle().isEmpty() && !recipe.getPassword().isEmpty()
							&& !recipe.getBody().isEmpty()) {
						newRecipe.get().setBody(recipe.getBody());
						newRecipe.get().setTitle(recipe.getTitle());
						newRecipe.get().setEstimatedTime(recipe.getEstimatedTime());
						newRecipe.get().setUsername(recipe.getUsername());
						newRecipe.get().setDate(Instant.now());
						recipeRepository.save(newRecipe.get());
					}
				} else {
					throw new RecipeException("Recipe not found");
				}
			} else {
				throw new UserException("Invalid Username or Password");
			}
		} catch (RecipeException e) {
			throw new RecipeException(e.getMessage());
		}

		return newRecipe.get();
	}

	@Override
	public Boolean deleteRecipe(Long recipeId, String username, String password) throws RecipeException, UserException {
		log.info("Class: RecipeServiceImpl and method: deleteRecipe");
		User checkUser = userRepository.findByUsernameAndPassword(username, password);
		boolean result = false;
		try {
			if (checkUser != null) {
				Optional<User> checkRecipe = userRepository.findById(recipeId);
				if (checkRecipe != null) {
					recipeRepository.deleteById(recipeId);
					result = true;
				} else {
					throw new RecipeException("Recipe id not found");
				}
			} else {
				throw new UserException("Username or Password incorrect");
			}
		} catch (RecipeException e) {
			throw new RecipeException(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Recipe> getAllRecipeByUser(String username, String password) throws RecipeException, UserException {
		log.info("Class: RecipeServiceImpl and method: getAllRecipeByUser");
		User checkUser = userRepository.findByUsernameAndPassword(username, password);
		log.info("user: {}", checkUser);
		List<Recipe> allRecipe = null;
		try {
			if (checkUser != null) {
				allRecipe = recipeRepository.findAllByUsername(username);
				log.info("all recipe:{}", allRecipe);
				if (allRecipe == null) {
					throw new RecipeException("No recipe found");
				}
			} else {
				throw new UserException("Invalid User");
			}
		} catch (UserException e) {
			throw new UserException(e.getMessage());
		} catch (RecipeException e) {
			throw new RecipeException(e.getMessage());
		}

		return allRecipe;
	}

}
