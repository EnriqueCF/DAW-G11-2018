package com.daw.contafin.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CompletedExercise {

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne //Unidirectional, 1: N 1 user can be in many completed exercises
	private User user;
	
	@ManyToOne //Unidirectional, 1: N 1 exercise can be in many completed exercises
	private Exercise exercise;
	
	private long error;
	
	public CompletedExercise() {
		
	}
	public CompletedExercise(User user,Exercise exercise, long error) {
		this.user=user;
		this.error=error;
		this.exercise=exercise;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/*public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}*/

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}
	public long getError() {
		return error;
	}

	public void setErrores(long error) {
		this.error = error;
	}

}
