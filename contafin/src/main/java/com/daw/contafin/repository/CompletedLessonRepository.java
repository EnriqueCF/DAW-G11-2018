package com.daw.contafin.repository;


import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.contafin.model.CompletedLesson;
import com.daw.contafin.model.Lesson;
import com.daw.contafin.model.User;


public interface CompletedLessonRepository extends JpaRepository <CompletedLesson, Long>{
	List<CompletedLesson> findByUserAndDate(User user, Date date);
	CompletedLesson findByUserAndLesson(User user, Lesson lesson);
	List<CompletedLesson> findByUser(User user);
}
