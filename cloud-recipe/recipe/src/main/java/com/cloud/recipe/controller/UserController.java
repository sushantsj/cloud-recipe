package com.cloud.recipe.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.recipe.entity.User;
import com.cloud.recipe.exceptions.UserException;
import com.cloud.recipe.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@PostMapping("/create-user")
	public ResponseEntity<Boolean> createUser(@Valid @RequestBody User user) throws UserException {
		log.info("REST request to create new user : {}", user);
		boolean result = false;
		try {
			if (user != null)
				result = userService.createUser(user);
		} catch (UserException e) {
			throw new UserException(e.getMessage());
		}
		return new ResponseEntity<Boolean>(result, HttpStatus.CREATED);
	}

	@DeleteMapping("/delete-user/{username}/{password}")
	public ResponseEntity<Boolean> deleteUser(@PathVariable("username") String username,
			@PathVariable("password") String password) throws UserException {
		log.info("REST request ot delete user");
		boolean result = false;
		try {
			if (username != null && password != null)
				result = userService.deleteUser(username, password);
		} catch (UserException e) {
			throw new UserException(e.getMessage());
		}
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);

	}
}
