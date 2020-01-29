package com.daw.contafin.service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.daw.contafin.model.CompletedLesson;
import com.daw.contafin.model.Lesson;
import com.daw.contafin.model.Unit;
import com.daw.contafin.model.User;
import com.daw.contafin.repository.LessonRepository;
import com.daw.contafin.user.UserComponent;

@Service
public class LessonService {
	
	@Autowired
	LessonRepository lessonRepository;
	
	@Autowired
	ExerciseService exerciseService;
	
	@Autowired
	CompletedLessonService completedLessonService;
	
	@Autowired
	CompletedExerciseService completedExerciseService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	LessonService lessonService;
	
	@Autowired
	UserComponent userComponent;
	
	
	public Lesson findById (long id) {
		return lessonRepository.findById(id);
	}
	public List<Lesson> findByUnit(Unit Id){
		return lessonRepository.findByUnit(Id);
	}
	public void save(Lesson lesson) {
		lessonRepository.save(lesson);
	}
	
	public List<Lesson> findAll(){
		return lessonRepository.findAll();
	}
	
	public void delete(long Id) {
		lessonRepository.deleteById(Id);
	}
	public Page<Lesson> getLessons(Pageable page) {
		return lessonRepository.findAll(page);
	}
	public void updateUserData(User user, Date sqlDate) {
		user.setFluency(exerciseService.getFluency(user));
		user.setExp(user.getExp() + 10);
		user.upLevel();
		user.updateStreak(user, completedLessonService.getCompletedLessons(user, sqlDate));
		user.setRemainingGoals(userService.getRemainingGoals(user));
		user.setLastLesson(userService.getLesson(user));
		user.setLastUnit(userService.getUnit(user));
		userService.updateUserData(user);
	}

	public void completedLesson(User user, int idlesson, int idunit, int numExercisesCompleted) {
		Lesson lesson = lessonService.findById(idlesson + (3 * (idunit - 1)));
		Calendar date = Calendar.getInstance();
		Date sqlDate = new Date((date.getTime()).getTime());
		CompletedLesson completedLessonS = completedLessonService.findByUserAndLesson(user, lesson);
		// If lesson is not completed yet and you do all exercise set the Lesson to done
		if ((completedLessonS == null) && (numExercisesCompleted == 4)) {
			CompletedLesson completedLesson = new CompletedLesson(user, lesson, sqlDate);
			completedLessonService.save(completedLesson);
			if (userComponent.isLoggedUser()) {
				user.setFluency(exerciseService.getFluency(user));
				lessonService.updateUserData(user, sqlDate);
				userComponent.setLoggedUser(user);
			}
		}
	}
}

