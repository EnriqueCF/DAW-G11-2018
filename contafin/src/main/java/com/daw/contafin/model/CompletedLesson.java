package com.daw.contafin.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class CompletedLesson {

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne //Unidirectional, 1: N 1 user can be in many completed exercises.
	private User user;
	
	@ManyToOne //Unidirectional, 1: N 1 exercise can be in many completed exercises
	private Lesson lesson;
	
	private Date date;
	
	public CompletedLesson() {
		
	}
	public CompletedLesson(User user, Lesson lesson, Date date) {
		this.user = user;
		this.lesson = lesson;
		this.date = date;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}

}
