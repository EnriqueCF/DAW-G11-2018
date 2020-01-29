package com.daw.contafin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.contafin.model.Answer;
import com.daw.contafin.model.Exercise;
import com.daw.contafin.repository.AnswerRepository;

@Service
public class AnswerService {
	
	@Autowired
	AnswerRepository answerRepository;
	
	public Answer findById (long id) {
		return answerRepository.findById(id);
	}
	public Answer findByExercise (Exercise exercise) {
		return answerRepository.findByExercise(exercise);
	}
	public void save(Answer answer) {
		answerRepository.save(answer);
	}
	public void delete(Answer answer) {
		answerRepository.delete(answer);
	}
}
