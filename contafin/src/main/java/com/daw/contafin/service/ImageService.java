package com.daw.contafin.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daw.contafin.model.Exercise;
import com.daw.contafin.repository.ExerciseRepository;
import com.daw.contafin.user.UserComponent;


@Service
public class ImageService {
	
	@Autowired
	ExerciseRepository exerciseRepository;
	
	@Autowired
	UserComponent userComponent;
	
	public byte[] uploadImage(MultipartFile file) throws IOException {
		
		//file to bytes
		byte[] bytes = file.getBytes();
		//Return an InputStream to read the contents of the file from.
		InputStream inputStream= file.getInputStream();
		//Read stream data into "bytes"
		inputStream.read(bytes);
		
		return bytes;
	}
	
	//Store exercises images located in the "static/img" folder in the database
	public void saveImages(Exercise exercise, Path route1, Path route2, Path route3) throws IOException {
		byte []image = Files.readAllBytes(route1);
		exercise.setImage1(image);
		image = Files.readAllBytes(route2);
		exercise.setImage2(image);
		image = Files.readAllBytes(route3);
		exercise.setImage3(image);
		exerciseRepository.save(exercise);	
	}
	
	//Show profile picture
	public void showImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		byte [] image;
		if (userComponent.getLoggedUser().getImage() != null) {
			image = userComponent.getLoggedUser().getImage();
		} else {
			Path path = Paths.get("img/profile.png");
			image = Files.readAllBytes(path);
		}
		response.setContentType("image/jpeg");
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(image);
		outputStream.close();
	}
	
	// Show exercise picture
	public void showImageExercise(Exercise exercise, long nImage, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		byte[] image;
		if (nImage == 1) {
			image = exercise.getImage1();
		} else if (nImage == 2) {
			image = exercise.getImage2();
		} else {
			image = exercise.getImage3();
		}
		response.setContentType("image/jpeg");
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(image);
		outputStream.close();
	}	
		
}
