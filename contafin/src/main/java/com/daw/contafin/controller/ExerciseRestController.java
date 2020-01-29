package com.daw.contafin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.daw.contafin.model.Answer;
import com.daw.contafin.model.CompletedExercise;
import com.daw.contafin.model.Exercise;
import com.daw.contafin.model.Exercise.ExerciseBassic;
import com.daw.contafin.model.Lesson;
import com.daw.contafin.model.User;
import com.daw.contafin.service.CompletedExerciseService;
import com.daw.contafin.service.ExerciseService;
import com.daw.contafin.service.ImageService;
import com.daw.contafin.service.LessonService;
import com.daw.contafin.service.UserService;
import com.daw.contafin.user.UserComponent;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/api/Unit")
@CrossOrigin(maxAge =3600)
public class ExerciseRestController{
	
	@Autowired
	private LessonService lessonService;
	
	@Autowired
	private ExerciseService exerciseService;
	
	@Autowired
	UserService userService;

	@Autowired
	UserComponent userComponent;

	@Autowired
	ImageService imageService;
	
	@Autowired
	CompletedExerciseService completedExerciseService;
	

	//See all the exercise
	@JsonView(ExerciseBassic.class)
	@GetMapping(value = "/Lesson/Exercises/")
	public ResponseEntity<Page<Exercise>> getExercises(Pageable page) {
		return new ResponseEntity<>(exerciseService.getExercises(page), HttpStatus.OK);
	}
		
	@JsonView(ExerciseBassic.class)
	@GetMapping(value = "/{idunit}/Lesson/{idlesson}/Exercise/{id}")
	public ResponseEntity<Exercise> getOneExercise(@PathVariable long idunit,@PathVariable long idlesson,@PathVariable long id) {
		Lesson lesson = lessonService.findById((idunit-1)*3+idlesson);
		Exercise exercise = exerciseService.findByLessonAndId(lesson, id);
		if (exercise != null) {
			return new ResponseEntity<>(exercise, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//change exercise
	@PostMapping(value = "/{idunit}/Lesson/{idlesson}/Exercise/{id}")
	public ResponseEntity<Exercise> updateExercise(@PathVariable long idunit,@PathVariable long idlesson,@PathVariable long id, @RequestBody Exercise exerciseAct) {
		Lesson lesson = lessonService.findById((idunit-1)*3+idlesson);
		Exercise exercise = exerciseService.findByLessonAndId(lesson, id);
		if (exercise != null) {
			exercise.setKind(exerciseAct.getKind());
			exercise.setStatement(exerciseAct.getStatement());
			exercise.setTexts(exerciseAct.getTexts());
			exerciseAct.setId(id);
			exerciseService.save(exercise);
			return new ResponseEntity<>(exercise, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//Ask for a answer
	@GetMapping(value = "/{idunit}/Lesson/{idlesson}/Exercise/{id}/Answer")
	public ResponseEntity<Answer> getOneAnswer(@PathVariable long idunit,@PathVariable long idlesson,@PathVariable long id) {
		Lesson lesson = lessonService.findById((idunit-1)*3+idlesson);
		Exercise exercise = exerciseService.findByLessonAndId(lesson, id);
		if (exercise != null) {
			return new ResponseEntity<>(exercise.getAnswer(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{idunit}/Lesson/{idlesson}/Exercise/{id}/Answer", method = RequestMethod.PUT)
	public ResponseEntity<Exercise> changeAnswer(@PathVariable long idunit,@PathVariable long idlesson,@PathVariable long id, @RequestBody Answer answerAct) {
		Lesson lesson = lessonService.findById((idunit-1)*3+idlesson);
		Exercise exercise = exerciseService.findByLessonAndId(lesson, id);
		if (exercise != null) {
			Answer answer = exercise.getAnswer();
			answer.setResult(answerAct.getResult());
			answerAct.setId(id);
			exercise.setAnswer(answer);
			exerciseService.save(exercise);
			return new ResponseEntity<>(exercise, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/{idunit}/Lesson/{idlesson}/Exercise/{id}/Solution", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> checkExercise(@PathVariable long idunit,@PathVariable long idlesson,@PathVariable long id, @RequestBody Answer answerAct) {
		User user = userComponent.getLoggedUser();
		Lesson lesson = lessonService.findById((idunit-1)*3+idlesson);
		Exercise exercise = exerciseService.findByLessonAndId(lesson, id);
		boolean goodanswer;
		if (exercise != null) {
			Answer answer = exercise.getAnswer();
			if(exercise.getKind()==2) {
				String[] answergood = answer.getResult().split(" ");
				String[] myanswer = answerAct.getResult().split(" ");
				int counter = 0;
	
				for (int i = 0; i < answergood.length; i++) {
					for (int j = 0; j < myanswer.length; j++) {
						if (answergood[i].equals(myanswer[j])) {
							counter++;
						}
					}
				}
				if (counter >= 3) {
					goodanswer=true;
					completedExerciseService.save(new CompletedExercise(user, exercise, 0));
					if (userComponent.isLoggedUser()) {
						user.updatePoints(user, 3);
						userService.save(user);
					}
				} else {
					goodanswer=false;
					if (userComponent.isLoggedUser()) {
						user.updatePoints(user, -3);
						userService.save(user);
					}
				}
			}
			else {
				if(answer.getResult().equals(answerAct.getResult())) {
					goodanswer=true;
					completedExerciseService.save(new CompletedExercise(user, exercise, 0));
					if (userComponent.isLoggedUser()) {
						user.updatePoints(user, 3);
						userService.save(user);
					}
				}
				else {
					goodanswer=false;
					if (userComponent.isLoggedUser()) {
						user.updatePoints(user, -3);
						userService.save(user);
					}
				}
			}
			return new ResponseEntity<>(goodanswer, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/DeleteAllExercises", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteExerciseCompleted() {
		User user = userComponent.getLoggedUser();
		exerciseService.deleteAll(user);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	

}


