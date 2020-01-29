package com.daw.contafin.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.contafin.model.User;
import com.daw.contafin.service.UserService;
import com.daw.contafin.user.UserComponent;


@RestController
@RequestMapping("/api")
@CrossOrigin(maxAge =3600)
public class LoginRestController {

	private static final Logger log = LoggerFactory.getLogger(LoginRestController.class);

	@Autowired
	private UserComponent userComponent;
	
	@Autowired
	private UserService userService;

	@RequestMapping("/login")
	public ResponseEntity<User> logIn() {

		if (!userComponent.isLoggedUser()) {
			log.info("Not user logged");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			User loggedUser = userComponent.getLoggedUser();
			log.info("Logged as " + loggedUser.getName());
			//Updated user last login date
			loggedUser.setLastConnection(loggedUser.newConnection());
			userService.updateUserData(loggedUser);
			userComponent.setLoggedUser(loggedUser);
			return new ResponseEntity<>(loggedUser, HttpStatus.OK);
		}
	}

	@RequestMapping("/logout")
	public ResponseEntity<Boolean> logOut(HttpSession session) {

		if (!userComponent.isLoggedUser()) {
			log.info("No user logged");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			session.invalidate();
			log.info("Logged out");
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
	}

}