package com.cloud.recipe.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.recipe.entity.User;
import com.cloud.recipe.exceptions.UserException;
import com.cloud.recipe.repository.UserRepository;
import com.cloud.recipe.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Boolean createUser(User user) throws UserException {
		log.info("Class: UserServiceImpl and method: createUser");
		User checkUser = userRepository.findByUsername(user.getUsername());
		boolean result = false;
		try {
			if (checkUser == null) {
				userRepository.save(user);
				result = true;
			} else if (checkUser != null) {
				throw new UserException("User already created, please choose another username");
			}
		} catch (UserException e) {
			throw new UserException("User already exists, please choose another username");
		}
		return result;
	}

	@Override
	public Boolean deleteUser(String username, String password) throws UserException {
		log.info("Class: UserServiceImpl and method: deleteUser");
		User checkUser = userRepository.findByUsername(username);
		log.info("User details: {}", checkUser);
		boolean result = false;
		try {
			try {
				if (checkUser != null && checkUser.getPassword().equalsIgnoreCase(password)) {
					userRepository.delete(checkUser);
					result = true;
				} else if (checkUser == null ){
					throw new UserException("User not found");
				} else {
					throw new UserException("Password incorrect");
				}
			} catch (UserException e) {
				throw new UserException(" Error deleting user "+ e.getMessage());
			}
		} catch (UserException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

}
