package com.daw.contafin.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.daw.contafin.model.User;


public interface UserRepository extends CrudRepository <User, Long>{
	User findByName (String name);
	User findByEmail (String email);
	User findById(long id);
	List <User> findAll();
	Page <User> findAll(Pageable page);
	
}
