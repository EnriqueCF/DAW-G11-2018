package com.daw.contafin.controller;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.contafin.model.User;
import com.daw.contafin.service.CompletedExerciseService;
import com.daw.contafin.service.CompletedLessonService;
import com.daw.contafin.service.EmailService;
import com.daw.contafin.service.ErrorMessage;
import com.daw.contafin.service.ExerciseService;
import com.daw.contafin.service.UserService;
import com.daw.contafin.user.UserComponent;

import freemarker.template.TemplateException;

@Controller
public class WebController extends ContentController {

	@Autowired
	UserService userService;

	@Autowired
	UserComponent userComponent;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	ErrorMessage errorMessage;
	
	@Autowired
	CompletedLessonService completedLessonService;
	
	@Autowired
	CompletedExerciseService completedExerciseService;
	
	@Autowired
	ExerciseService exerciseService;

	// Login Controller

	@RequestMapping("/")
	public String index() {

		return "index";
	}
	
	//Connect to Angular
	@RequestMapping("/new")
	public String connectToAngular() {
		return "new/index.html";
	}
	@RequestMapping("/new/")
	public String connectToAngular2() {
		return "new/index.html";
	}
	
	// Home page Controller

	@RequestMapping("home")
	public String home(Model model) {

		model.addAttribute("loggedUser", userComponent.isLoggedUser());

		User user = userComponent.getLoggedUser();
		//Delete exercises completed if you exit in the middle of a lesson.
		exerciseService.deleteAll(user);
		if (userComponent.isLoggedUser()) {
			
			if (user.getRoles().contains("ROLE_ADMIN")) {
				model.addAttribute("isAdmin", true);
			}
			loadNavbar(model);
			model.addAttribute("dailyGoal", userService.findByEmail(user.getEmail()).getDailyGoal());
			model.addAttribute("fluency", userService.findByEmail(user.getEmail()).getFluency());
			model.addAttribute("exp", userService.findByEmail(user.getEmail()).getExp());
			model.addAttribute("fluency", userService.findByEmail(user.getEmail()).getFluency()+" %");
			model.addAttribute("lessonsToDo",userService.getRemainingGoals(user));
			//Update user remaining Goals
			user.setRemainingGoals(userService.getRemainingGoals(user));
			//Update the user's last connection
			user.setLastConnection(user.newConnection());
			userService.updateUserData(user);
			userComponent.setLoggedUser(user);
		}
		loadUnits(model);
		return "home";
	}
	
	@RequestMapping ("Admin/Home")
	public String adminHomePage(Model model) {
		return "adminHome";
	}

	// Sign Up Controller

	@RequestMapping("signup")
	public String register(Model model, @RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("pass") String pass) {
		if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
			model.addAttribute("errorMessage","¡Error al crear la cuenta! Es necesario que rellenes todos los campos.");
		    return "error2";
		}
		if (userService.findByEmail(email) == null) {
			User user = new User(name, email, pass, "ROLE_USER");
			userService.save(user);
			user.setLastConnection(user.newConnection());
			userService.updateUserData(user);
			userComponent.setLoggedUser(user);
			try {
				emailService.sendSimpleMessage(userService.findByEmail(email));
			} catch (MessagingException messaginException) {
				System.out.println(messaginException);
			} catch (IOException IOexception) {
				System.out.println(IOexception);
			} catch (TemplateException templateException) {
				System.out.println(templateException);
			}
			;
			return "/";
		} else {
			model.addAttribute("errorMessage","¡Error al crear la cuenta! El correo introducido ya está vinculado a otra cuenta.");
		    return "error2";
		}

	}
	
	//Wrong login controller
	
	@RequestMapping("/loginerror")
	public String loginerror(Model model) {
		model.addAttribute("errorMessage","¡Error al iniciar sesión! Datos erróneos.");
	    return "error2";
	}
	

}
