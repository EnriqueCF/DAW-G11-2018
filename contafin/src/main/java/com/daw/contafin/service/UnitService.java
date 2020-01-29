package com.daw.contafin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.daw.contafin.model.Exercise;
import com.daw.contafin.model.Lesson;
import com.daw.contafin.model.Unit;
import com.daw.contafin.repository.UnitRepository;


@Service
public class UnitService {

	@Autowired
	UnitRepository unitRepository;
	
	public List<Unit> findAll(){
		return unitRepository.findAll();
	}
	
	public void save(Unit unit){
		unitRepository.save(unit);
	}
	
	public Unit findById(long Id) {
		return unitRepository.findById(Id);
	}
	
	public void delete(long Id) {
		unitRepository.deleteById(Id);
	}
	
	public Page<Unit> getUnits(Pageable page){
		return unitRepository.findAll(page);
	}
	
	public boolean isValidUnit(Unit unit) {
		
		List <Lesson> lessons = unit.getLessons();
		
		if(unit.getName().isEmpty()) {
			return false;
		}

		List <Exercise> exercises = unit.getLessons().get(0).getExercises();
		List <String> texts = unit.getLessons().get(0).getExercises().get(0).getTexts();
		for (int i=0; i<3; i++) {
			if(lessons.get(i).getName().isEmpty()) {
				return false;
			}
			for (int j=0; j<4; j++) {
				if(exercises.get(j).getStatement().isEmpty()) {
					return false;
				}
				if(exercises.get(j).getAnswer().getResult().isEmpty()) {
					return false;
				}
				for (int k=0; k<3; j++) {
					if(texts.get(k).isEmpty()) {
						return false;
					}
				}
				texts = unit.getLessons().get(i+1).getExercises().get(j+1).getTexts();
				
			}
			exercises = unit.getLessons().get(i+1).getExercises();
		}
		return true;
	}
}
