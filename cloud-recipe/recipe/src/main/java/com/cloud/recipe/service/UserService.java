package com.cloud.recipe.service;

import com.cloud.recipe.entity.User;
import com.cloud.recipe.exceptions.UserException;

public interface UserService {

	Boolean createUser(User user) throws UserException;
	
	Boolean deleteUser(String username, String password) throws UserException;
}
