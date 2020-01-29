package com.daw.contafin.model;

import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Exercise {


	public interface ExerciseBassic {}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(ExerciseBassic.class)
	private long id;

	@JsonIgnore
	@ManyToOne
	private Lesson lesson;

	@JsonView(ExerciseBassic.class)
	private int kind;

	@JsonView(ExerciseBassic.class)
	private String statement;

	@JsonIgnore
	@Lob
	private byte[] image1;

	@JsonIgnore
	@Lob
	private byte[] image2;

	@JsonIgnore
	@Lob
	private byte[] image3;
	
	@JsonView(ExerciseBassic.class)
	@ElementCollection
	private List<String> texts;

	@JsonIgnore
	@OneToMany(mappedBy = "exercise")
	private List<CompletedExercise> completedExercises;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	private Answer answer;

	public Exercise() {

	}

	public Exercise(int kind, String statement, List<String> texts, Answer answer, Lesson lesson) {
		this.kind = kind;
		this.statement = statement;
		this.texts = texts;
		this.setLesson(lesson);
		this.answer = answer;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public List<String> getTexts() {
		return texts;
	}

	public void setTexts(List<String> texts) {
		this.texts = texts;
	}

	public List<CompletedExercise> getCompletedExercises() {
		return completedExercises;
	}

	public void setCompletedExercises(List<CompletedExercise> completedExercises) {
		this.completedExercises = completedExercises;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public byte[] getImage1() {
		return image1;
	}

	public void setImage1(byte[] image1) {
		this.image1 = image1;
	}

	public byte[] getImage2() {
		return image2;
	}

	public void setImage2(byte[] image2) {
		this.image2 = image2;
	}

	public byte[] getImage3() {
		return image3;
	}

	public void setImage3(byte[] image3) {
		this.image3 = image3;
	}

	@Override
	public String toString() {
		return "Exercise [id=" + id + ", lesson=" + lesson + ", kind=" + kind + ", statement=" + statement + ", image1="
				+ Arrays.toString(image1) + ", image2=" + Arrays.toString(image2) + ", image3="
				+ Arrays.toString(image3) + ", texts=" + texts + ", completedExercises="
				+ completedExercises + ", answer=" + answer + "]";
	}

}
