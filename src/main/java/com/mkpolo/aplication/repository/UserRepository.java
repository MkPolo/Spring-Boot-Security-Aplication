package com.mkpolo.aplication.repository;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mkpolo.aplication.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	Optional<User> findByUsername(String username);

	//Optional<User> findByUsernameOrEmail(String username, String email);
	
}
