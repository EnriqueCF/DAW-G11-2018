package com.daw.contafin.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.contafin.model.CompletedExercise;
import com.daw.contafin.model.Exercise;
import com.daw.contafin.model.Lesson;
import com.daw.contafin.model.User;
import com.daw.contafin.repository.CompletedExerciseRepository;

@Service
public class CompletedExerciseService {
	
	@Autowired
	CompletedExerciseRepository completedExerciseRepository;
	
	@Autowired
	LessonService lessonService;
	
	@Autowired
	ExerciseService exerciseService;
	
	
	public void save(CompletedExercise completedExercise) {
		completedExerciseRepository.save(completedExercise);
	}
	public CompletedExercise findByUserAndExercise(User user, Exercise exercise){
		return completedExerciseRepository.findByUserAndExercise(user,exercise);
	}
	
	public List<CompletedExercise> findByUser(User user){
		return completedExerciseRepository.findByUser(user);
	}
	
	public void delete(CompletedExercise completedExercise) {
		completedExerciseRepository.delete(completedExercise);
	}
	public void delete(long Id) {
		completedExerciseRepository.deleteById(Id);
	}
	//Get all ExerciseCompleted in the lesson and delete them (need to put wrong exercise last)
	public int numExercisesCompleted(long idlesson, int idunit, User user) {
		int numExercisesCompleted = 0;
		Lesson lesson = lessonService.findById(idlesson + (3 * (idunit - 1)));
		List<Exercise> listExercises = exerciseService.findByLesson(lesson);
		for (int i = 0; i < listExercises.size(); i++) {
			CompletedExercise completedExerciseS = completedExerciseRepository.findByUserAndExercise(user,listExercises.get(i));
			if (completedExerciseS != null) {
				numExercisesCompleted++;
				completedExerciseRepository.delete(completedExerciseS);
			}
		}
		return  numExercisesCompleted;
	}
	
}