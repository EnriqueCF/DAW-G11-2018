package com.daw.contafin.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Unit {

	public interface UnitBassic {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	@JsonView(UnitBassic.class)
	private long id;

	@JsonView(UnitBassic.class)
	private String name;

	@OneToMany(mappedBy = "unit")
	private List<Lesson> lessons;

	public Unit() {
	}

	public Unit(String name) {
		this.name = name;
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

	public List<Lesson> getLessons() {
		return this.lessons;
	}

	public void setLesson(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	@Override
	public String toString() {
		return "Unit [id=" + id + ", name=" + name + "]";
	}

}
