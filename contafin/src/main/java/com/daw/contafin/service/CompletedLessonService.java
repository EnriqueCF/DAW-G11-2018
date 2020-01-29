package com.daw.contafin.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.contafin.model.CompletedLesson;
import com.daw.contafin.model.Lesson;
import com.daw.contafin.model.User;
import com.daw.contafin.repository.CompletedLessonRepository;

@Service
public class CompletedLessonService {
	
	@Autowired
	CompletedLessonRepository completedLessonRepository;
	
	public int getCompletedLessons(User user, Date date) {
		return completedLessonRepository.findByUserAndDate(user, date).size();
	}

	public List<CompletedLesson> findByUserAndDate(User user, Date date){
		return completedLessonRepository.findByUserAndDate(user, date);
	}
	
	public CompletedLesson findByUserAndLesson(User user, Lesson lesson) {
		return completedLessonRepository.findByUserAndLesson(user, lesson);
	}
	
	public List<CompletedLesson> findByUser(User user){
		return completedLessonRepository.findByUser(user);
	}
	
	public void save(CompletedLesson completedLesson) {
		completedLessonRepository.save(completedLesson);
	}
	public void delete(long Id) {
		completedLessonRepository.deleteById(Id);
	}
}
