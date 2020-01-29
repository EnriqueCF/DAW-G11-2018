package com.daw.contafin.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Lesson {
	
	public interface LessonBasic {}

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	
	@JsonView(LessonBasic.class)
	private long id;
	
	@JsonView(LessonBasic.class)
	private String name;
	
	@OneToMany (mappedBy = "lesson")
	private List<Exercise> exercises;    
	
	@JsonIgnore
	@ManyToOne
	private Unit unit;
	
	public Lesson() {}
	public Lesson(String name, Unit unit) {
		this.name=name;
		this.setUnit(unit);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	public List<Exercise> getExercises() {
		return exercises;
	}
	public void setExercises(List<Exercise> exercises) {
		this.exercises = exercises;
	}
	
	
}
