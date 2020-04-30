package com.mkpolo.aplication.service;

import com.mkpolo.aplication.entity.User;

public interface UserService {

	public Iterable<User> getAllUsers();

	public User createUser(User user) throws Exception;
	
}
