package com.daw.contafin.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.contafin.model.CompletedExercise;
import com.daw.contafin.model.Exercise;
import com.daw.contafin.model.Lesson;
import com.daw.contafin.model.User;
import com.daw.contafin.service.CompletedExerciseService;
import com.daw.contafin.service.ExerciseService;
import com.daw.contafin.service.LessonService;
import com.daw.contafin.service.UserService;
import com.daw.contafin.user.UserComponent;

@Controller
public class ExerciseController {

	@Autowired
	private ExerciseService exerciseService;

	@Autowired
	private LessonService lessonService;

	@Autowired
	private CompletedExerciseService completedExerciseService;

	@Autowired
	UserComponent userComponent;

	@Autowired
	UserService userService;

	List<String> texts;
	User user;

	int points = 0;

	@PostConstruct
	public void init() {

	}

	@RequestMapping("/Unit/{id}/lessons/{numLesson}/Exercise/1/{numExercise}/Completed")
	public String exerciseCompleted1(Model model, @PathVariable int id, @PathVariable int numLesson,
			@PathVariable int numExercise, @RequestParam String solution) {

		user = userComponent.getLoggedUser();

		Lesson lesson = lessonService.findById(numLesson + (3 * (id - 1)));

		Exercise exercise = exerciseService.findByLessonAndId(lesson, numExercise);

		Exercise nextExercise = exerciseService.findByLessonAndId(lesson, numExercise + 1);

		String answer = exercise.getAnswer().getResult();

		if (solution.equals(answer)) {
			if (solution.equals("uno")) {
				model.addAttribute("exercise1", "exercise1Good");
				model.addAttribute("exercise2", "exercise1");
				model.addAttribute("exercise3", "exercise1");
				completedExerciseService.save(new CompletedExercise(user, exercise, 0));
				points = points + 3;

			} else if (solution.equals("dos")) {
				model.addAttribute("exercise1", "exercise1");
				model.addAttribute("exercise2", "exercise1Good");
				model.addAttribute("exercise3", "exercise1");
				completedExerciseService.save(new CompletedExercise(user, exercise, 0));
				points = points + 3;
			} else {
				model.addAttribute("exercise1", "exercise1");
				model.addAttribute("exercise2", "exercise1");
				model.addAttribute("exercise3", "exercise1Good");
				completedExerciseService.save(new CompletedExercise(user, exercise, 0));
				points = points + 3;
			}
		} else {
			if (solution.equals("uno")) {
				model.addAttribute("exercise1", "exercise1Bad");
				model.addAttribute("exercise2", "exercise1");
				model.addAttribute("exercise3", "exercise1");
				points = points - 3;
			} else if (solution.equals("dos")) {
				model.addAttribute("exercise1", "exercise1");
				model.addAttribute("exercise2", "exercise1Bad");
				model.addAttribute("exercise3", "exercise1");
				points = points - 3;
			} else {
				model.addAttribute("exercise1", "exercise1");
				model.addAttribute("exercise2", "exercise1");
				model.addAttribute("exercise3", "exercise1Bad");
				points = points - 3;
			}
		}

		model.addAttribute("text1", exercise.getTexts().get(0));
		model.addAttribute("text2", exercise.getTexts().get(1));
		model.addAttribute("text3", exercise.getTexts().get(2));
		model.addAttribute("statement", exercise.getStatement());

		// With this code you get the wrong exercise to go to the end
		CompletedExercise nextCompletedExercise = completedExerciseService.findByUserAndExercise(user, nextExercise);
		if (nextExercise == null || nextCompletedExercise != null) {

			List<Exercise> exercises = exerciseService.findByLesson(lesson);
			for (int i = 0; i < exercises.size(); i++) {
				CompletedExercise completedExerciseS = completedExerciseService.findByUserAndExercise(user,
						exercises.get(i));
				if (completedExerciseS == null) {
					nextExercise = exercises.get(i);
					int typeNext = nextExercise.getKind();
					long nextNumExercise = nextExercise.getId();
					model.addAttribute("next", false);
					model.addAttribute("correct", true);
					model.addAttribute("end", false);
					model.addAttribute("tocorrectend", false);
					model.addAttribute("idunit", id);
					model.addAttribute("nextNumExercise", nextNumExercise);
					model.addAttribute("nextType", typeNext);
					model.addAttribute("idunit", id);
					model.addAttribute("idlesson", numLesson);
					model.addAttribute("thisExercise", numExercise);
					return "exerciseType1";
				}
			}
			//

			model.addAttribute("next", false);
			model.addAttribute("end", true);
			model.addAttribute("correct", false);
			model.addAttribute("tocorrectend", false);
		} else {
			int typeNext = nextExercise.getKind();
			long nextNumExercise = nextExercise.getId();
			model.addAttribute("next", false);
			model.addAttribute("correct", true);
			model.addAttribute("end", false);
			model.addAttribute("tocorrectend", false);
			model.addAttribute("idunit", id);
			model.addAttribute("nextNumExercise", nextNumExercise);
			model.addAttribute("nextType", typeNext);
		}

		model.addAttribute("idunit", id);
		model.addAttribute("idlesson", numLesson);
		model.addAttribute("thisExercise", numExercise);

		return "exerciseType1";
	}

	@RequestMapping("/Unit/{id}/lessons/{numLesson}/Exercise/1/{numExercise}")
	public String exercise1(Model model, @PathVariable int id, @PathVariable int numLesson,
			@PathVariable int numExercise) {

		model.addAttribute("exercise1", "exercise1");
		model.addAttribute("exercise2", "exercise1");
		model.addAttribute("exercise3", "exercise1");

		Lesson lesson = lessonService.findById(numLesson + (3 * (id - 1)));

		Exercise exercise = exerciseService.findByLessonAndId(lesson, numExercise);

		Exercise nextExercise = exerciseService.findByLessonAndId(lesson, numExercise + 1);

		model.addAttribute("text1", exercise.getTexts().get(0));
		model.addAttribute("text2", exercise.getTexts().get(1));
		model.addAttribute("text3", exercise.getTexts().get(2));
		model.addAttribute("statement", exercise.getStatement());

		CompletedExercise nextCompletedExercise = completedExerciseService.findByUserAndExercise(user, nextExercise);
		if (nextExercise == null || nextCompletedExercise != null) {

			List<Exercise> exercises = exerciseService.findByLesson(lesson);
			for (int i = 0; i < exercises.size(); i++) {
				CompletedExercise completedExerciseS = completedExerciseService.findByUserAndExercise(user,
						exercises.get(i));
				if (completedExerciseS == null) {
					nextExercise = exercises.get(i);
					int typeNext = nextExercise.getKind();
					long nextNumExercise = nextExercise.getId();

					model.addAttribute("next", true);
					model.addAttribute("correct", false);
					model.addAttribute("end", false);
					model.addAttribute("tocorrectend", false);
					model.addAttribute("nextNumExercise", nextNumExercise);
					model.addAttribute("nextType", typeNext);
					model.addAttribute("idunit", id);
					model.addAttribute("idlesson", numLesson);
					model.addAttribute("thisExercise", numExercise);
					return "exerciseType1";
				}
			}

			model.addAttribute("next", false);
			model.addAttribute("correct", false);
			model.addAttribute("end", false);
			model.addAttribute("tocorrectend", true);
		} else {
			int typeNext = nextExercise.getKind();
			long nextNumExercise = nextExercise.getId();

			model.addAttribute("next", true);
			model.addAttribute("correct", false);
			model.addAttribute("end", false);
			model.addAttribute("tocorrectend", false);
			model.addAttribute("nextNumExercise", nextNumExercise);
			model.addAttribute("nextType", typeNext);

		}
		model.addAttribute("idunit", id);
		model.addAttribute("idlesson", numLesson);
		model.addAttribute("thisExercise", numExercise);

		return "exerciseType1";
	}

	@RequestMapping("/Unit/{id}/lessons/{numLesson}/Exercise/2/{numExercise}/Completed")
	public String exerciseCompleted2(Model model, @PathVariable int id, @PathVariable int numLesson,
			@PathVariable int numExercise, @RequestParam String solution) {

		Lesson lesson = lessonService.findById(numLesson + (3 * (id - 1)));

		Exercise exercise = exerciseService.findByLessonAndId(lesson, numExercise);

		Exercise nextExercise = exerciseService.findByLessonAndId(lesson, numExercise + 1);

		String answerCorrect = exercise.getAnswer().getResult();

		String[] answer = answerCorrect.split(" ");
		String[] solutionparts = solution.split(" ");
		int counter = 0;

		for (int i = 0; i < solutionparts.length; i++) {
			for (int j = 0; j < answer.length; j++) {
				if (solutionparts[i].equals(answer[j])) {
					counter++;
				}
			}
		}
		if (counter >= 3) {
			model.addAttribute("color", "exercise1Good");
			points = points + 3;
			completedExerciseService.save(new CompletedExercise(user, exercise, 0));
		} else {
			model.addAttribute("color", "exercise1Bad");
			points = points - 3;
		}

		model.addAttribute("statement", exercise.getStatement());

		CompletedExercise nextCompletedExercise = completedExerciseService.findByUserAndExercise(user, nextExercise);
		if (nextExercise == null || nextCompletedExercise != null) {

			List<Exercise> exercises = exerciseService.findByLesson(lesson);
			for (int i = 0; i < exercises.size(); i++) {
				CompletedExercise completedExerciseS = completedExerciseService.findByUserAndExercise(user,
						exercises.get(i));
				if (completedExerciseS == null) {
					nextExercise = exercises.get(i);
					int typeNext = nextExercise.getKind();
					long nextNumExercise = nextExercise.getId();
					model.addAttribute("next", false);
					model.addAttribute("correct", true);
					model.addAttribute("end", false);
					model.addAttribute("tocorrectend", false);
					model.addAttribute("idunit", id);
					model.addAttribute("nextNumExercise", nextNumExercise);
					model.addAttribute("nextType", typeNext);
					model.addAttribute("idunit", id);
					model.addAttribute("idlesson", numLesson);
					model.addAttribute("thisExercise", numExercise);
					return "exerciseType2";
				}
			}

			model.addAttribute("next", false);
			model.addAttribute("end", true);
			model.addAttribute("correct", false);
			model.addAttribute("tocorrectend", false);
		} else {
			int typeNext = nextExercise.getKind();
			long nextNumExercise = nextExercise.getId();
			model.addAttribute("next", false);
			model.addAttribute("correct", true);
			model.addAttribute("end", false);
			model.addAttribute("tocorrectend", false);
			model.addAttribute("idunit", id);
			model.addAttribute("nextNumExercise", nextNumExercise);
			model.addAttribute("nextType", typeNext);
		}

		model.addAttribute("idunit", id);
		model.addAttribute("idlesson", numLesson);
		model.addAttribute("thisExercise", numExercise);

		return "exerciseType2";
	}

	@RequestMapping("/Unit/{id}/lessons/{numLesson}/Exercise/2/{numExercise}")
	public String exercise2(Model model, @PathVariable int id, @PathVariable int numLesson,
			@PathVariable int numExercise) {

		model.addAttribute("color", "exercise2");

		Lesson lesson = lessonService.findById(numLesson + (3 * (id - 1)));

		Exercise exercise = exerciseService.findByLessonAndId(lesson, numExercise);

		Exercise nextExercise = exerciseService.findByLessonAndId(lesson, numExercise + 1);

		model.addAttribute("statement", exercise.getStatement());

		CompletedExercise nextCompletedExercise = completedExerciseService.findByUserAndExercise(user, nextExercise);
		if (nextExercise == null || nextCompletedExercise != null) {

			List<Exercise> exercises = exerciseService.findByLesson(lesson);
			for (int i = 0; i < exercises.size(); i++) {
				CompletedExercise completedExerciseS = completedExerciseService.findByUserAndExercise(user,
						exercises.get(i));
				if (completedExerciseS == null) {
					nextExercise = exercises.get(i);
					int typeNext = nextExercise.getKind();
					long nextNumExercise = nextExercise.getId();

					model.addAttribute("next", true);
					model.addAttribute("correct", false);
					model.addAttribute("end", false);
					model.addAttribute("tocorrectend", false);
					model.addAttribute("nextNumExercise", nextNumExercise);
					model.addAttribute("nextType", typeNext);
					model.addAttribute("idunit", id);
					model.addAttribute("idlesson", numLesson);
					model.addAttribute("thisExercise", numExercise);

					return "exerciseType2";
				}
			}

			model.addAttribute("next", false);
			model.addAttribute("correct", false);
			model.addAttribute("end", false);
			model.addAttribute("tocorrectend", true);
		} else {
			int typeNext = nextExercise.getKind();
			long nextNumExercise = nextExercise.getId();

			model.addAttribute("next", true);
			model.addAttribute("correct", false);
			model.addAttribute("end", false);
			model.addAttribute("tocorrectend", false);
			model.addAttribute("nextNumExercise", nextNumExercise);
			model.addAttribute("nextType", typeNext);

		}
		model.addAttribute("idunit", id);
		model.addAttribute("idlesson", numLesson);
		model.addAttribute("thisExercise", numExercise);

		return "exerciseType2";
	}

	/*
	 * @RequestMapping("/Unit/{id}/lessons/{numLesson}/Exercise/3/{numExercise}")
	 * public String exercise3(Model model,@PathVariable int id, @PathVariable int
	 * numLesson,@PathVariable int numExercise) {
	 * 
	 * Lesson lesson = lessonRepository.findById(numLesson+(3*(id-1))); Exercise
	 * exercise = exerciseService.findByLessonAndId(lesson, numExercise);
	 * 
	 * Exercise nextExercise = exerciseService.findByLessonAndId(lesson,
	 * numExercise+1); texts = exerciseService.findById(numExercise).getTexts();
	 * 
	 * 
	 * model.addAttribute("Statement", exercise.getStatement());
	 * model.addAttribute("texts", texts);
	 * 
	 * if (nextExercise==null) { model.addAttribute("next",false);
	 * model.addAttribute("end",true); } else { int typeNext =
	 * nextExercise.getKind(); long nextNumExercise = nextExercise.getId();
	 * 
	 * model.addAttribute("next",true); model.addAttribute("end",false);
	 * model.addAttribute("idunit",id);
	 * model.addAttribute("nextNumExercise",nextNumExercise);
	 * model.addAttribute("nextType",typeNext); }
	 * model.addAttribute("idlesson",numLesson);
	 * 
	 * return "exerciseType3"; }
	 * 
	 * @RequestMapping("/Unit/{id}/lessons/{numLesson}/Exercise/4/{numExercise}")
	 * public String exercise4(Model model,@PathVariable int id, @PathVariable int
	 * numLesson,@PathVariable int numExercise) {
	 * 
	 * Lesson lesson = lessonRepository.findById(numLesson+(3*(id-1))); Exercise
	 * exercise = exerciseService.findByLessonAndId(lesson, numExercise);
	 * 
	 * Exercise nextExercise = exerciseService.findByLessonAndId(lesson,
	 * numExercise+1); texts = exerciseService.findById(numExercise).getTexts();
	 * 
	 * model.addAttribute("Statement", exercise.getStatement());
	 * model.addAttribute("texts", texts);
	 * 
	 * if (nextExercise==null) { model.addAttribute("next",false);
	 * model.addAttribute("end",true); } else { int typeNext =
	 * nextExercise.getKind(); long nextNumExercise = nextExercise.getId();
	 * 
	 * model.addAttribute("next",true); model.addAttribute("end",false);
	 * model.addAttribute("idunit",id);
	 * model.addAttribute("nextNumExercise",nextNumExercise);
	 * model.addAttribute("nextType",typeNext); }
	 * model.addAttribute("idlesson",numLesson);
	 * 
	 * return "exerciseType4"; }
	 */

	@RequestMapping("/Unit/{id}/lessons/{numLesson}/Exercise/5/{numExercise}/Completed")
	public String exerciseCompleted5(Model model, @PathVariable int id, @PathVariable int numLesson,
			@PathVariable int numExercise, @RequestParam String solution) {

		Lesson lesson = lessonService.findById(numLesson + (3 * (id - 1)));

		Exercise exercise = exerciseService.findByLessonAndId(lesson, numExercise);

		Exercise nextExercise = exerciseService.findByLessonAndId(lesson, numExercise + 1);

		String answer = exercise.getAnswer().getResult();

		if (solution.equals(answer)) {
			if (solution.equals("uno")) {
				model.addAttribute("exercise1", "exercise1Good");
				model.addAttribute("exercise2", "exercise1");
				model.addAttribute("exercise3", "exercise1");
				completedExerciseService.save(new CompletedExercise(user, exercise, 0));
				points = points + 3;
			} else if (solution.equals("dos")) {
				model.addAttribute("exercise1", "exercise1");
				model.addAttribute("exercise2", "exercise1Good");
				model.addAttribute("exercise3", "exercise1");
				completedExerciseService.save(new CompletedExercise(user, exercise, 0));
				points = points + 3;
			} else {
				model.addAttribute("exercise1", "exercise1");
				model.addAttribute("exercise2", "exercise1");
				model.addAttribute("exercise3", "exercise1Good");
				completedExerciseService.save(new CompletedExercise(user, exercise, 0));
				points = points + 3;
			}
		} else {
			if (solution.equals("uno")) {
				model.addAttribute("exercise1", "exercise1Bad");
				model.addAttribute("exercise2", "exercise1");
				model.addAttribute("exercise3", "exercise1");
				points = points - 3;
			} else if (solution.equals("dos")) {
				model.addAttribute("exercise1", "exercise1");
				model.addAttribute("exercise2", "exercise1Bad");
				model.addAttribute("exercise3", "exercise1");
				points = points - 3;
			} else {
				model.addAttribute("exercise1", "exercise1");
				model.addAttribute("exercise2", "exercise1");
				model.addAttribute("exercise3", "exercise1Bad");
				points = points - 3;
			}
		}

		texts = exerciseService.findById(numExercise).getTexts();

		model.addAttribute("text1", texts.get(0));
		model.addAttribute("text2", texts.get(1));
		model.addAttribute("text3", texts.get(2));
		model.addAttribute("statement", exercise.getStatement());

		CompletedExercise nextCompletedExercise = completedExerciseService.findByUserAndExercise(user, nextExercise);
		if (nextExercise == null || nextCompletedExercise != null) {

			List<Exercise> exercises = exerciseService.findByLesson(lesson);
			for (int i = 0; i < exercises.size(); i++) {
				CompletedExercise completedExerciseS = completedExerciseService.findByUserAndExercise(user,
						exercises.get(i));
				if (completedExerciseS == null) {
					nextExercise = exercises.get(i);
					int typeNext = nextExercise.getKind();
					long nextNumExercise = nextExercise.getId();
					model.addAttribute("next", false);
					model.addAttribute("correct", true);
					model.addAttribute("end", false);
					model.addAttribute("tocorrectend", false);
					model.addAttribute("idunit", id);
					model.addAttribute("nextNumExercise", nextNumExercise);
					model.addAttribute("nextType", typeNext);
					model.addAttribute("idunit", id);
					model.addAttribute("idlesson", numLesson);
					model.addAttribute("thisExercise", numExercise);
					return "exerciseType5";
				}
			}

			model.addAttribute("next", false);
			model.addAttribute("end", true);
			model.addAttribute("correct", false);
			model.addAttribute("tocorrectend", false);
		} else {
			int typeNext = nextExercise.getKind();
			long nextNumExercise = nextExercise.getId();
			model.addAttribute("next", false);
			model.addAttribute("correct", true);
			model.addAttribute("end", false);
			model.addAttribute("tocorrectend", false);
			model.addAttribute("idunit", id);
			model.addAttribute("nextNumExercise", nextNumExercise);
			model.addAttribute("nextType", typeNext);
		}
		model.addAttribute("idunit", id);
		model.addAttribute("idlesson", numLesson);
		model.addAttribute("thisExercise", numExercise);

		return "exerciseType5";
	}

	@RequestMapping("/Unit/{id}/lessons/{numLesson}/Exercise/5/{numExercise}")
	public String exercise5(Model model, @PathVariable int id, @PathVariable int numLesson,
			@PathVariable int numExercise) {

		model.addAttribute("exercise1", "exercise1");
		model.addAttribute("exercise2", "exercise1");
		model.addAttribute("exercise3", "exercise1");

		Lesson lesson = lessonService.findById(numLesson + (3 * (id - 1)));
		Exercise exercise = exerciseService.findByLessonAndId(lesson, numExercise);

		Exercise nextExercise = exerciseService.findByLessonAndId(lesson, numExercise + 1);
		texts = exerciseService.findById(numExercise).getTexts();

		model.addAttribute("text1", texts.get(0));
		model.addAttribute("text2", texts.get(1));
		model.addAttribute("text3", texts.get(2));
		model.addAttribute("statement", exercise.getStatement());

		CompletedExercise nextCompletedExercise = completedExerciseService.findByUserAndExercise(user, nextExercise);
		if (nextExercise == null || nextCompletedExercise != null) {

			List<Exercise> exercises = exerciseService.findByLesson(lesson);
			for (int i = 0; i < exercises.size(); i++) {
				CompletedExercise completedExerciseS = completedExerciseService.findByUserAndExercise(user,
						exercises.get(i));
				if (completedExerciseS == null) {
					nextExercise = exercises.get(i);
					int typeNext = nextExercise.getKind();
					long nextNumExercise = nextExercise.getId();

					model.addAttribute("next", true);
					model.addAttribute("correct", false);
					model.addAttribute("end", false);
					model.addAttribute("tocorrectend", false);
					model.addAttribute("nextNumExercise", nextNumExercise);
					model.addAttribute("nextType", typeNext);
					model.addAttribute("idunit", id);
					model.addAttribute("idlesson", numLesson);
					model.addAttribute("thisExercise", numExercise);
					return "exerciseType5";
				}
			}

			model.addAttribute("next", false);
			model.addAttribute("correct", false);
			model.addAttribute("end", false);
			model.addAttribute("tocorrectend", true);
		} else {
			int typeNext = nextExercise.getKind();
			long nextNumExercise = nextExercise.getId();

			model.addAttribute("next", true);
			model.addAttribute("correct", false);
			model.addAttribute("end", false);
			model.addAttribute("tocorrectend", false);
			model.addAttribute("nextNumExercise", nextNumExercise);
			model.addAttribute("nextType", typeNext);

		}
		model.addAttribute("idunit", id);
		model.addAttribute("idlesson", numLesson);
		model.addAttribute("thisExercise", numExercise);

		return "exerciseType5";
	}
	/*
	 * @RequestMapping("/Unit/{id}/lessons/{numLesson}/Exercise/6/{numExercise}")
	 * public String exercise6(Model model,@PathVariable int id, @PathVariable int
	 * numLesson, @PathVariable int numExercise) {
	 * 
	 * Lesson lesson = lessonRepository.findById(numLesson+(3*(id-1))); Exercise
	 * exercise = exerciseService.findByLessonAndId(lesson, numExercise); Exercise
	 * nextExercise = exerciseService.findByLessonAndId(lesson, numExercise+1);
	 * 
	 * String texts1 = exercise.getTexts().get(0); String texts2 =
	 * exercise.getTexts().get(1); String texts3 = exercise.getTexts().get(2);
	 * String texts4 = exercise.getTexts().get(3);
	 * 
	 * model.addAttribute("Statement", exercise.getStatement());
	 * model.addAttribute("texts1", texts1); model.addAttribute("texts2", texts2);
	 * model.addAttribute("texts3", texts3); model.addAttribute("texts4", texts4);
	 * 
	 * if (nextExercise==null) { model.addAttribute("next",false);
	 * model.addAttribute("end",true); } else { int typeNext =
	 * nextExercise.getKind(); long nextNumExercise = nextExercise.getId();
	 * 
	 * model.addAttribute("next",true); model.addAttribute("end",false);
	 * model.addAttribute("idunit",id);
	 * model.addAttribute("nextNumExercise",nextNumExercise);
	 * model.addAttribute("nextType",typeNext); }
	 * model.addAttribute("idlesson",numLesson);
	 * 
	 * return "exerciseType6"; }
	 */

	@RequestMapping("/Unit/{id}/lessons/{numLesson}/Exercise/7/{numExercise}/Completed")
	public String exerciseCompleted7(Model model, @PathVariable int id, @PathVariable int numLesson,
			@PathVariable int numExercise, @RequestParam String solution) {
		
		Lesson lesson = lessonService.findById(numLesson + (3 * (id - 1)));

		Exercise exercise = exerciseService.findByLessonAndId(lesson, numExercise);

		Exercise nextExercise = exerciseService.findByLessonAndId(lesson, numExercise + 1);

		String answer = exercise.getAnswer().getResult();

		if (solution.equals(answer)) {
			if (solution.equals("uno")) {
				model.addAttribute("exercise1", "exercise1Good");
				model.addAttribute("exercise2", "exercise1");
				model.addAttribute("exercise3", "exercise1");
				completedExerciseService.save(new CompletedExercise(user, exercise, 0));
				points = points + 3;
			} else if (solution.equals("dos")) {
				model.addAttribute("exercise1", "exercise1");
				model.addAttribute("exercise2", "exercise1Good");
				model.addAttribute("exercise3", "exercise1");
				completedExerciseService.save(new CompletedExercise(user, exercise, 0));
				points = points + 3;
			} else {
				model.addAttribute("exercise1", "exercise1");
				model.addAttribute("exercise2", "exercise1");
				model.addAttribute("exercise3", "exercise1Good");
				completedExerciseService.save(new CompletedExercise(user, exercise, 0));
				points = points + 3;
			}
		} else {
			if (solution.equals("uno")) {
				model.addAttribute("exercise1", "exercise1Bad");
				model.addAttribute("exercise2", "exercise1");
				model.addAttribute("exercise3", "exercise1");
				points = points - 3;
			} else if (solution.equals("dos")) {
				model.addAttribute("exercise1", "exercise1");
				model.addAttribute("exercise2", "exercise1Bad");
				model.addAttribute("exercise3", "exercise1");
				points = points - 3;
			} else {
				model.addAttribute("exercise1", "exercise1");
				model.addAttribute("exercise2", "exercise1");
				model.addAttribute("exercise3", "exercise1Bad");
				points = points - 3;
			}
		}

		texts = exerciseService.findById(numExercise).getTexts();

		model.addAttribute("text1", texts.get(0));
		model.addAttribute("text2", texts.get(1));
		model.addAttribute("text3", texts.get(2));
		model.addAttribute("statement", exercise.getStatement());

		CompletedExercise nextCompletedExercise = completedExerciseService.findByUserAndExercise(user, nextExercise);
		if (nextExercise == null || nextCompletedExercise != null) {

			List<Exercise> exercises = exerciseService.findByLesson(lesson);
			for (int i = 0; i < exercises.size(); i++) {
				CompletedExercise completedExerciseS = completedExerciseService.findByUserAndExercise(user,
						exercises.get(i));
				if (completedExerciseS == null) {
					nextExercise = exercises.get(i);
					int typeNext = nextExercise.getKind();
					long nextNumExercise = nextExercise.getId();
					model.addAttribute("next", false);
					model.addAttribute("correct", true);
					model.addAttribute("end", false);
					model.addAttribute("tocorrectend", false);
					model.addAttribute("idunit", id);
					model.addAttribute("nextNumExercise", nextNumExercise);
					model.addAttribute("nextType", typeNext);
					model.addAttribute("idunit", id);
					model.addAttribute("idlesson", numLesson);
					model.addAttribute("thisExercise", numExercise);
					return "exerciseType7";
				}
			}

			model.addAttribute("next", false);
			model.addAttribute("end", true);
			model.addAttribute("correct", false);
			model.addAttribute("tocorrectend", false);
		} else {
			int typeNext = nextExercise.getKind();
			long nextNumExercise = nextExercise.getId();
			model.addAttribute("next", false);
			model.addAttribute("correct", true);
			model.addAttribute("end", false);
			model.addAttribute("tocorrectend", false);
			model.addAttribute("idunit", id);
			model.addAttribute("nextNumExercise", nextNumExercise);
			model.addAttribute("nextType", typeNext);
		}
		model.addAttribute("idunit", id);
		model.addAttribute("idlesson", numLesson);
		model.addAttribute("thisExercise", numExercise);

		return "exerciseType7";
	}

	@RequestMapping("/Unit/{id}/lessons/{numLesson}/Exercise/7/{numExercise}")
	public String exercise7(Model model, @PathVariable int id, @PathVariable int numLesson,
			@PathVariable int numExercise) {

		model.addAttribute("exercise1", "exercise1");
		model.addAttribute("exercise2", "exercise1");
		model.addAttribute("exercise3", "exercise1");

		Lesson lesson = lessonService.findById(numLesson + (3 * (id - 1)));

		Exercise exercise = exerciseService.findByLessonAndId(lesson, numExercise);

		Exercise nextExercise = exerciseService.findByLessonAndId(lesson, numExercise + 1);

		model.addAttribute("text1", exercise.getTexts().get(0));
		model.addAttribute("text2", exercise.getTexts().get(1));
		model.addAttribute("text3", exercise.getTexts().get(2));
		model.addAttribute("statement", exercise.getStatement());

		CompletedExercise nextCompletedExercise = completedExerciseService.findByUserAndExercise(user, nextExercise);
		if (nextExercise == null || nextCompletedExercise != null) {

			List<Exercise> exercises = exerciseService.findByLesson(lesson);
			for (int i = 0; i < exercises.size(); i++) {
				CompletedExercise completedExerciseS = completedExerciseService.findByUserAndExercise(user,
						exercises.get(i));
				if (completedExerciseS == null) {
					nextExercise = exercises.get(i);
					int typeNext = nextExercise.getKind();
					long nextNumExercise = nextExercise.getId();

					model.addAttribute("next", true);
					model.addAttribute("correct", false);
					model.addAttribute("end", false);
					model.addAttribute("tocorrectend", false);
					model.addAttribute("nextNumExercise", nextNumExercise);
					model.addAttribute("nextType", typeNext);
					model.addAttribute("idunit", id);
					model.addAttribute("idlesson", numLesson);
					model.addAttribute("thisExercise", numExercise);

					return "exerciseType7";
				}
			}

			model.addAttribute("next", false);
			model.addAttribute("correct", false);
			model.addAttribute("end", false);
			model.addAttribute("tocorrectend", true);
		} else {
			int typeNext = nextExercise.getKind();
			long nextNumExercise = nextExercise.getId();

			model.addAttribute("next", true);
			model.addAttribute("correct", false);
			model.addAttribute("end", false);
			model.addAttribute("tocorrectend", false);
			model.addAttribute("nextNumExercise", nextNumExercise);
			model.addAttribute("nextType", typeNext);

		}
		model.addAttribute("idunit", id);
		model.addAttribute("idlesson", numLesson);
		model.addAttribute("thisExercise", numExercise);

		return "exerciseType7";
	}

	@RequestMapping("/Unit/{id}/lesson/{idlesson}/lessonCompleted/")
	public String completedLesson(Model model, @PathVariable int idlesson, @PathVariable int id) {
		
		user = userComponent.getLoggedUser();
		//Get all ExerciseCompleted in the lesson and delete them (need to put wrong exercise last)
		int numExercisesCompleted = completedExerciseService.numExercisesCompleted(idlesson, id, user);
		//Update user data
		lessonService.completedLesson(user, idlesson, id, numExercisesCompleted);
		if(numExercisesCompleted == 4) {
			return "completedLesson";
		
		//You can go to completedLesson if lesson it is not done	
		}else {
			return "error2";
		}
		
		
	}

	@RequestMapping("/continueLesson")
	public String continueLesson(Model model) {
			User user = userComponent.getLoggedUser();

			int percentage = exerciseService.getFluency(user);
			if (userComponent.isLoggedUser()) {

				user.setFluency(percentage);
				user.updatePoints(user,points);
				userService.updateUserData(user);
				userComponent.setLoggedUser(user);
			}
			model.addAttribute("percentage", percentage);


		return "continueLesson";

	}
	
	// Show the image
	@RequestMapping("Image/{numImage}/{numExercise}")
	public void sowImage1(HttpServletRequest request, HttpServletResponse response, @PathVariable int numImage,
			@PathVariable int numExercise) throws IOException {
		byte[] image;
		if (numImage == 1) {
			image = exerciseService.findById(numExercise).getImage1();
		} else if (numImage == 2) {
			image = exerciseService.findById(numExercise).getImage2();
		} else {
			image = exerciseService.findById(numExercise).getImage3();
		}

		response.setContentType("image/jpeg");
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(image);
		outputStream.close();
	}

}
