package com.mkpolo.aplication.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mkpolo.aplication.entity.User;
import com.mkpolo.aplication.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository repository;
	
	@Override
	public Iterable<User> getAllUsers() {
		return repository.findAll();
	}
	
	private boolean checkUsernameAvailable(User user) throws Exception {
		Optional<User> userFound = repository.findByUsername(user.getUsername());
		
		if(userFound.isPresent()) {
			throw new Exception("Username no disponible");
		}
		return true;
	}
	
	private boolean checkPasswordValid(User user) throws Exception{
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			throw new Exception("Las contraseñas no coinciden");
		}
		return true;
	}

	@Override
	public User createUser(User user) throws Exception {
		if(checkUsernameAvailable(user) && checkPasswordValid(user)) {
			repository.save(user);
		}
		return user;
	}

}
