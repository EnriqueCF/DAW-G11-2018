package com.daw.contafin.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Answer {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;
    private String result;
    
    @JsonIgnore
    @OneToOne(mappedBy="answer")
    private Exercise exercise;
    
    public Answer() {}
    public Answer(String result) {
        
        this.result=result;
        
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public Exercise getExercise() {
        return exercise;
    }
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
    
    
}
