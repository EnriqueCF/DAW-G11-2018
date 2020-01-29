package com.daw.contafin.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.contafin.model.CompletedExercise;
import com.daw.contafin.model.Exercise;
import com.daw.contafin.model.User;


public interface CompletedExerciseRepository extends JpaRepository <CompletedExercise, Long>{
	CompletedExercise findByUserAndExercise(User user, Exercise exercise);
	List<CompletedExercise> findByUser(User user);
}
