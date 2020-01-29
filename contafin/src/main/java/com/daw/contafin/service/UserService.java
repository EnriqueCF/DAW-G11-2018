package com.daw.contafin.service;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.daw.contafin.model.CompletedLesson;
import com.daw.contafin.model.User;
import com.daw.contafin.repository.UserRepository;
import com.daw.contafin.user.UserComponent;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserComponent userComponent;
	
	@Autowired
	CompletedLessonService completedLessonService;
	
	public User findByEmail (String email) {
		return userRepository.findByEmail(email);
	}
	
	public List<User> getUsers () {
		return userRepository.findAll();
	}
	
	public void save(User user) {
		userRepository.save(user);
	}
	
	public User findById(long id) {
		return userRepository.findById(id);
	}
	
	public void updateUserData(User user) {
		userRepository.save(user);
	}
	
	public void deleteAccount(User user) {
		userRepository.delete(user);
	}
	
	public int  getLesson(User user) {
		List <CompletedLesson> completedLesson = completedLessonService.findByUser(user);
		int lesson = completedLesson.size() % 3;
		return lesson;
	}
	
	public int  getUnit(User user) {
		List <CompletedLesson> completedLesson = completedLessonService.findByUser(user);
		int unit = (int) (completedLesson.size()/3) + 1;
		return unit;
	}
	
	public int [] progress(User user) {
		//Create an array for weekly progress
		int [] progress = new int[7];
		//User user = userComponent.getLoggedUser();
		//Get the current date and set first day of week Monday
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		//Convert java.util.Date to java.sql.Date
		Date date= calendar.getTime();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		//Get the current day
		int day =calendar.get(Calendar.DAY_OF_WEEK);
		//Store weekly progress
		progress[day-1]=completedLessonService.getCompletedLessons(user, sqlDate);
		for (int i= day-1; i< 0; i-- ) { 
			calendar.add(Calendar.DATE, -1);
			date= calendar.getTime();
			sqlDate= new java.sql.Date(date.getTime());
			progress[i-1]=completedLessonService.getCompletedLessons(user, sqlDate);
		}
		return progress;
	}
	
	public Page<User> getUsers(Pageable page) {
		return userRepository.findAll(page);
	}
	
	public int getRemainingGoals(User user) {
		Calendar date = Calendar.getInstance();
		java.sql.Date sqlDate =  new java.sql.Date((date.getTime()).getTime());
		if (completedLessonService.getCompletedLessons(user, sqlDate) >= user.getDailyGoal()) {
			return 0;
		} else {
			return (user.getDailyGoal() - completedLessonService.getCompletedLessons(user, sqlDate));
		}
	}
	
	public User updateUser(User user, User updatedUser) {
		//Change goal
		if(updatedUser.getDailyGoal() !=0) {
			user.setDailyGoal(updatedUser.getDailyGoal());
			user.setRemainingGoals(updatedUser.getDailyGoal());
			updateUserData(user);
			userComponent.setLoggedUser(user);
			return user;
		}
		//Change name, email or password
		if (updatedUser.getName().isEmpty() && updatedUser.getEmail().isEmpty() && updatedUser.getPasswordHash().isEmpty()) {
			return null;
		} else {
			if (!updatedUser.getName().isEmpty()) {
				user.setName(updatedUser.getName());
			}
			if (!updatedUser.getEmail().isEmpty()) {
				user.setEmail(updatedUser.getEmail());
			}
			if(!updatedUser.getPasswordHash().isEmpty()) {
				String passwordHash = new BCryptPasswordEncoder().encode(updatedUser.getPasswordHash());
				user.setPasswordHash(passwordHash);
			}
			updateUserData(user);
			userComponent.setLoggedUser(user);
			return user;
		}
	}
	
}
