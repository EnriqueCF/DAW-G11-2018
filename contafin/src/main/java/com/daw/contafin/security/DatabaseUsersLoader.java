package com.daw.contafin.security;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.daw.contafin.model.User;
import com.daw.contafin.repository.UserRepository;

@Component
public class DatabaseUsersLoader {

	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	private void initDatabase() {

		userRepository.save(new User("user", "email@hotmail.es", "pass", "ROLE_USER"));
		userRepository.save(new User("admin", "adminemail@hotmail.es", "adminpass", "ROLE_ADMIN","ROLE_USER"));
		userRepository.save(new User("Sergio", "sergio@hotmail.es", "pass", 2, 50, 7, 3, "ROLE_USER"));

		// Add test users.
		for (int i = 0; i <= 40; i++) {
			userRepository.save(new User("user " + i, "email" + i + "@hotmail.es", "pass", "ROLE_USER"));
		}
	}

}
