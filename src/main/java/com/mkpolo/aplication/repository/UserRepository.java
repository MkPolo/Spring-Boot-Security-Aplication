package com.mkpolo.aplication.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mkpolo.aplication.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	
}
