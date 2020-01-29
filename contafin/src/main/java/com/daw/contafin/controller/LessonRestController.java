package com.daw.contafin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.daw.contafin.model.CompletedLesson;
import com.daw.contafin.model.Lesson;
import com.daw.contafin.model.Unit;
import com.daw.contafin.model.User;
import com.daw.contafin.model.Lesson.LessonBasic;
import com.daw.contafin.service.CompletedExerciseService;
import com.daw.contafin.service.CompletedLessonService;
import com.daw.contafin.service.ExerciseService;
import com.daw.contafin.service.ImageService;
import com.daw.contafin.service.LessonService;
import com.daw.contafin.service.UnitService;
import com.daw.contafin.service.UserService;
import com.daw.contafin.user.UserComponent;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/api/Unit")
@CrossOrigin(maxAge =3600)
public class LessonRestController{
	
	@Autowired
	private UnitService unitService;
	
	@Autowired
	private LessonService lessonService;
	
	@Autowired
	UserService userService;

	@Autowired
	UserComponent userComponent;

	@Autowired
	ImageService imageService;
	
	@Autowired
	CompletedLessonService completedLessonService;
	
	@Autowired
	CompletedExerciseService completedExerciseService;
	
	@Autowired
	ExerciseService exerciseService;

	//See all the lessons
	@JsonView(LessonBasic.class)
	@RequestMapping(value = "/Lessons/", method = RequestMethod.GET)
	public ResponseEntity<Page<Lesson>> getLessons(Pageable page) {
		return new ResponseEntity<>(lessonService.getLessons(page), HttpStatus.OK);
	}
	
	//See an unit with its lessons
	@RequestMapping(value = "/{idunit}/Lesson/", method = RequestMethod.GET)
	public ResponseEntity<Unit> getunitwithlesson(@PathVariable long idunit) {
		Unit unit = unitService.findById(idunit);
		if (unit != null) {
			List<Lesson> lessons = unit.getLessons();
			for (int i=0; i<lessons.size();i++) {
				lessons.get(i).setExercises(null);
			}
			Unit unittry = new Unit(unit.getName());
			unittry.setId(unit.getId());
			unittry.setLesson(lessons);
			return new ResponseEntity<>(unittry, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//See one lesson
	@JsonView(LessonBasic.class)
	@RequestMapping(value = "/{idunit}/Lesson/{id}", method = RequestMethod.GET)
	public ResponseEntity<Lesson> getLesson(@PathVariable long idunit,@PathVariable long id) {
		Lesson lesson = lessonService.findById((idunit-1)*3+id);

		if (lesson != null) {
			return new ResponseEntity<>(lesson, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//Update a lesson
	@JsonView(LessonBasic.class)
	@RequestMapping(value = "/{idunit}/Lesson/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Lesson> updateLesson(@PathVariable long idunit,@PathVariable long id, @RequestBody Lesson lessonAct) {
		Lesson lesson = lessonService.findById((idunit-1)*3+id);
		if (lesson != null) {
			lesson.setName(lessonAct.getName());
			lessonAct.setId(id);
			lessonService.save(lesson);
			return new ResponseEntity<>(lesson, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{idunit}/Lesson/{idlesson}/Completed", method = RequestMethod.GET)
	public ResponseEntity<Boolean> completedLesson(@PathVariable int idunit, @PathVariable int idlesson) {
		User user = userComponent.getLoggedUser();
		// Get all ExerciseCompleted in the lesson and delete them (need to put wrong exercise last)
		int numExercisesCompleted = completedExerciseService.numExercisesCompleted(idlesson, idunit, user);
		//Update user data
		lessonService.completedLesson(user, idlesson, idunit, numExercisesCompleted);
		if (numExercisesCompleted == 4) {
			return new ResponseEntity<>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(false, HttpStatus.OK);
		}

	}
	
	@RequestMapping(value = "/{idunit}/Lesson/{idlesson}/isCompleted", method = RequestMethod.GET)
	public ResponseEntity<Boolean> isCompletedLesson(@PathVariable int idunit, @PathVariable int idlesson) {
		User user = userComponent.getLoggedUser();
		Lesson lesson = lessonService.findById(idlesson);
		CompletedLesson completedLesson = completedLessonService.findByUserAndLesson(user, lesson);
		if (completedLesson != null) {
			return new ResponseEntity<>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(false, HttpStatus.OK);
		}

	}
	
	@RequestMapping(value = "/{idunit}/Lessons/Completed", method = RequestMethod.GET)
	public ResponseEntity<ArrayList<Boolean>> isCompletedLessonB(@PathVariable int idunit) {
		User user = userComponent.getLoggedUser();
		List<Lesson> lessons = unitService.findById(idunit).getLessons();
		ArrayList<Boolean> booleans = new ArrayList<Boolean>(); 
		for (int i=0; i< lessons.size(); i++) {
			CompletedLesson completedLesson = completedLessonService.findByUserAndLesson(user, lessons.get(i));
			if(completedLesson != null) {
				booleans.add(true);
			}
			else {
				booleans.add(false);
			}
		}
		return new ResponseEntity<>(booleans, HttpStatus.OK);
	}
}

