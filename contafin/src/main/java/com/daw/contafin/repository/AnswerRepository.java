package com.daw.contafin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.contafin.model.Answer;
import com.daw.contafin.model.Exercise;


public interface AnswerRepository extends JpaRepository <Answer, Long>{
	Answer findByExercise (Exercise exercise);
	Answer findById(long Id);
}
