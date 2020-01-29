package com.daw.contafin.model;

public class UnitsComplete {

	private String name;
	
	private int lessonsDone = 0;
	
	private boolean done = false;
	
	public UnitsComplete(String name) {
		this.name=name;	
	}
	
	public int getLessonsDone() {
		return lessonsDone;
	}

	public void setLessonsDone(int lessonsDone) {
		this.lessonsDone = lessonsDone;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
