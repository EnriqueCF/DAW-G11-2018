package com.daw.contafin.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daw.contafin.model.CompletedExercise;
import com.daw.contafin.model.CompletedLesson;
import com.daw.contafin.model.Exercise;
import com.daw.contafin.model.Lesson;
import com.daw.contafin.model.User;
import com.daw.contafin.repository.ExerciseRepository;

@Service
public class ExerciseService {

	
		@Autowired
		ExerciseRepository exerciseRepository;
		
		@Autowired
		ImageService imageService;
		
		@Autowired
		LessonService lessonService;
		
		@Autowired
		CompletedLessonService completedLessonService;
		
		@Autowired
		CompletedExerciseService completedExerciseService;
		
		public Exercise findById (long id) {
			return exerciseRepository.findById(id);
		}
		
		public List<Exercise> findAll(){
			return exerciseRepository.findAll();
		}
		
		public Exercise findByLessonAndId (Lesson lesson, long id) {
			return exerciseRepository.findByLessonAndId(lesson,id);
		}
		
		public Exercise findByLessonAndKind(Lesson lesson, int kind) {
			return exerciseRepository.findByLessonAndKind(lesson,kind);
		}
		
		public List <Exercise> findByLesson(Lesson lesson) {
			 return exerciseRepository.findByLesson(lesson);
		}
		public void save(Exercise exercise) {
			exerciseRepository.save(exercise);
		}
		public void delete(long id) {
			exerciseRepository.deleteById(id);
		}
		public Page<Exercise> getExercises(Pageable page) {
			return exerciseRepository.findAll(page);
		}
		
		public void deleteAll(User user) {
			List<CompletedExercise> completedExercises =  completedExerciseService.findByUser(user);
			if (completedExercises != null) {
				for (int i=0; i<completedExercises.size();i++) {
					CompletedExercise completedExerciseS = completedExercises.get(i);
					if (completedExerciseS != null) {
						completedExerciseService.delete(completedExerciseS);
					}
				}
			}
		}
		
		//Upload exercise images
		public void uploadExerciseImages( Exercise exercise, MultipartFile image1, MultipartFile image2,MultipartFile image3) throws IOException{
			byte[] bytes1 = imageService.uploadImage(image1);
			byte[]bytes2 = imageService.uploadImage(image2);
			byte[] bytes3 = imageService.uploadImage(image3);
			exercise.setImage1(bytes1);
			exercise.setImage2(bytes2);
			exercise.setImage3(bytes3);
		}
		
		public int getFluency(User user) {
			List<Lesson> lessons = lessonService.findAll();
			List<CompletedLesson> lessonsCompleted = completedLessonService.findByUser(user);
			int numLessons = lessons.size();
			int numLessonsCompleted = lessonsCompleted.size();
			double percentageD = (double) numLessonsCompleted / numLessons * 100;
			int percentage = (int) percentageD;
			return percentage;
		}
		
}
