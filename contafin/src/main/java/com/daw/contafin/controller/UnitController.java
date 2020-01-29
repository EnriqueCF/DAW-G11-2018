package com.daw.contafin.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.daw.contafin.model.Answer;
import com.daw.contafin.model.Exercise;
import com.daw.contafin.model.Lesson;
import com.daw.contafin.model.Unit;
import com.daw.contafin.repository.LessonRepository;
import com.daw.contafin.service.ExerciseService;
import com.daw.contafin.service.ImageService;
import com.daw.contafin.service.UnitService;
import com.daw.contafin.service.UserService;
import com.daw.contafin.user.UserComponent;

@Controller
public class UnitController extends ContentController {

	@Autowired
	private UnitService unitService;

	@Autowired
	private LessonRepository lessonService;

	@Autowired
	private ExerciseService exerciseService;

	@Autowired
	UserService userService;

	@Autowired
	UserComponent userComponent;
	
	@Autowired
	ImageService imageService;
	
	Exercise exercise;

	@RequestMapping("/UpdateExercise")  
	public String unitCreation(Model model) {
		model.addAttribute("loggedUser", userComponent.isLoggedUser());

		if (userComponent.isLoggedUser()) {
			if (userComponent.getLoggedUser().getRoles().contains("ROLE_ADMIN")) {
				model.addAttribute("isAdmin", true);
			}
			loadNavbar(model);
		}
		return "unitCreation";
	}

	@RequestMapping("/UnitCreation")
	public String unit(Model model, @RequestParam String unitName, @RequestParam(value="lessonName[]") String[] lessonName,
			@RequestParam(value="images[]") MultipartFile[] images, @RequestParam(value="texts[]") String[] texts, @RequestParam(value="statements[]") String[] statements,
			@RequestParam(value="answers[]") String[] answers) throws IOException {

		Unit unit;
		unit = new Unit(unitName);
		unitService.save(unit);

		Lesson lesson1 = new Lesson(lessonName[0], unit);
		lessonService.save(lesson1);
		Lesson lesson2 = new Lesson(lessonName[1], unit);
		lessonService.save(lesson2);
		Lesson lesson3 = new Lesson(lessonName[2], unit);
		lessonService.save(lesson3);
		
		//Lesson 1
		Lesson lesson = lesson1;
		List<String> myTexts = Arrays.asList(texts[0], texts[1], texts[2]);
		Answer answer = new Answer(answers[0]);
		exercise=new Exercise(1, statements[0], myTexts, answer, lesson);
		exerciseService.uploadExerciseImages(exercise, images[0],images[1], images[2]);
		exerciseService.save(exercise);

		answer = new Answer(answers[1]);
		exerciseService.save(new Exercise(2, statements[1], null, answer, lesson));


		myTexts = Arrays.asList(texts[3], texts[4], texts[5]);
		answer = new Answer(answers[2]);
		exerciseService.save(new Exercise(5, statements[2], myTexts, answer, lesson));


		myTexts = Arrays.asList(texts[6], texts[7], texts[8]);
		answer = new Answer(answers[3]);
		exerciseService.save(new Exercise(7, statements[3], myTexts, answer, lesson));

		//Lesson 2
		lesson = lesson2;
		myTexts = Arrays.asList(texts[9], texts[10], texts[11]);
		answer = new Answer(answers[4]);
		exercise=new Exercise(1, statements[4], myTexts, answer, lesson);
		exerciseService.uploadExerciseImages(exercise, images[3],images[4], images[5]);
		exerciseService.save(exercise);

		answer = new Answer(answers[5]);
		exerciseService.save(new Exercise(2, statements[5], null, answer, lesson));

		myTexts = Arrays.asList(texts[12], texts[13], texts[14]);
		answer = new Answer(answers[6]);
		exerciseService.save(new Exercise(5, statements[6], myTexts, answer, lesson));

		myTexts = Arrays.asList(texts[15], texts[16], texts[17]);
		answer = new Answer(answers[7]);
		exerciseService.save(new Exercise(7, statements[7], myTexts, answer, lesson));

		//Lesson 3
		lesson = lesson3;
		myTexts = Arrays.asList(texts[18], texts[19], texts[20]);
		answer = new Answer(answers[8]);
		exercise=new Exercise(1, statements[8], myTexts, answer, lesson);
		exerciseService.uploadExerciseImages(exercise, images[6],images[7], images[8]);
		exerciseService.save(exercise);
		

		answer = new Answer(answers[9]);
		exerciseService.save(new Exercise(2, statements[9], null, answer, lesson));

		myTexts = Arrays.asList(texts[21], texts[22], texts[23]);
		answer = new Answer(answers[10]);
		exerciseService.save(new Exercise(5, statements[10], myTexts, answer, lesson));

		myTexts = Arrays.asList(texts[24], texts[25], texts[26]);  
		answer = new Answer(answers[11]);
		exerciseService.save(new Exercise(7, statements[11], myTexts, answer, lesson));

		return "adminHome";
	}

	@PostConstruct
	public void init() throws IOException  {


		Unit unit;
		unit = new Unit("Unidad 1");
		unitService.save(unit);
		unit = new Unit("Unidad 2");
		unitService.save(unit);

		unit = unitService.findById(1);
		Lesson lesson1 = new Lesson("Lección 1 Unidad 1", unit);
		lessonService.save(lesson1);
		Lesson lesson2 = new Lesson("Lección 2 Unidad 1", unit);
		lessonService.save(lesson2);
		Lesson lesson3 = new Lesson("Lección 3 Unidad 1", unit);
		lessonService.save(lesson3);

		unit = unitService.findById(2);
		Lesson lesson4 = new Lesson("Lección 1 Unidad 2", unit);
		lessonService.save(lesson4);
		Lesson lesson5 = new Lesson("Lección 2 Unidad 2", unit);
		lessonService.save(lesson5);
		Lesson lesson6 = new Lesson("Lección 3 Unidad 2", unit);
		lessonService.save(lesson6);
		
		//Unit 1 Lesson 1
		Lesson lesson = lessonService.findById(1);
		
		//Exercise 1
		Answer answer = new Answer("uno");
		List<String> texts = Arrays.asList("213.Maquinaria", "210.Terrenos y bienes naturales", "218. Elementos de transporte");
		exercise = new Exercise(1, "1.1.1 Seleccione el asiento", texts, answer, lesson);
		// Save the images in the database
		imageService.saveImages(exercise, Paths.get("img/machine.jpg"),
				Paths.get("img/land.jpg"),
				Paths.get("img/truck.jpg"));
		
		//Exercise 2
		answer = new Answer("Este es un texto de prueba en el que comprobarlo");
		exerciseService.save(new Exercise(2, "1.1.2 Escribe la denominación de la cuenta que recoge: "
				+ "maquinarias para el proceso productivo de la empresa", null, answer, lesson));


		//Exercise 5
		answer = new Answer("tres");
		texts = Arrays.asList("Activo", "Pasivo", "Patrimonio neto");
		exerciseService.save(
				new Exercise(5, "1.1.5 Escoge la respuesta correcta para la cuenta: 210. Terrenos y bienes naturales",
						 texts, answer, lesson));

		////Exercise 7
		answer = new Answer("dos");
		texts = Arrays.asList(
				" La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará integramente dentro de 10 años a través de la letra de cambio.",
				"La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará integramente dentro de 10 años.",
				" La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará en un plazo no superior a un año.");
		exerciseService.save(new Exercise(7,
				"1.1.7 Escoge el enunciado correcto para el asiento: \"10 211. Construcciones a 174. Provedores de inmovilizado a l/p 10\"",
				 texts, answer, lesson));

		//Unit 1 Lesson 2
		lesson = lessonService.findById(2);
		
		//Exercise 1
		answer = new Answer("uno");
		texts = Arrays.asList("213.Maquinaria", "210.Terrenos y bienes naturales", "218. Elementos de transporte");
		exercise = new Exercise(1, "1.1.1 Seleccione el asiento", texts, answer, lesson);
		//Save the images in the database
		imageService.saveImages(exercise, Paths.get("img/machine.jpg"),
				Paths.get("img/land.jpg"),
				Paths.get("img/truck.jpg"));
		
		//Exercixe 2
		answer = new Answer("Este es un texto de prueba en el que comprobarlo");
		exerciseService.save(new Exercise(2, "1.2.2 Escribe la denominación de la cuenta que recoge: "
				+ "maquinarias para el proceso productivo de la empresa", null, answer, lesson));


		//Exercise 5
		answer = new Answer("uno");
		texts = Arrays.asList("Activo", "Pasivo", "Patrimonio neto");
		exerciseService.save(
				new Exercise(5, "1.2.5 Escoge la respuesta correcta para la cuenta: 210. Terrenos y bienes naturales",
						 texts, answer, lesson));


		////Exercise 7
		answer = new Answer("uno");
		texts = Arrays.asList(
				" La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará integramente dentro de 10 años a través de la letra de cambio.",
				"La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará integramente dentro de 10 años.",
				" La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará en un plazo no superior a un año.");
		exerciseService.save(new Exercise(7,
				"1.2.7 Escoge el enunciado correcto para el asiento: \"10 211. Construcciones a 174. Provedores de inmovilizado a l/p 10\"",
				 texts, answer, lesson));

		//Unit 1 Lesson 3
		lesson = lessonService.findById(3);
		
		//Exercise 1
		answer = new Answer("uno");
		texts = Arrays.asList("213.Maquinaria", "210.Terrenos y bienes naturales", "218. Elementos de transporte");
		exercise = new Exercise(1, "1.3.1 Seleccione el asiento",  texts, answer, lesson);
		//Save the images in the database
		imageService.saveImages(exercise, Paths.get("img/machine.jpg"),
				Paths.get("img/land.jpg"),
				Paths.get("img/truck.jpg"));
				
		//Exercise 2
		answer = new Answer("Este es un texto de prueba en el que comprobarlo");
		exerciseService.save(new Exercise(2, "1.3.2 Escribe la denominación de la cuenta que recoge: "
				+ "maquinarias para el proceso productivo de la empresa", null, answer, lesson));

		//Exercise 5
		answer = new Answer("uno");
		texts = Arrays.asList("Activo", "Pasivo", "Patrimonio neto");
		exerciseService.save(
				new Exercise(5, "1.3.5 Escoge la respuesta correcta para la cuenta: 210. Terrenos y bienes naturales",
						 texts, answer, lesson));

		//Exercise 7
		answer = new Answer("uno");
		texts = Arrays.asList(
				" La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará integramente dentro de 10 años a través de la letra de cambio.",
				"La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará integramente dentro de 10 años.",
				" La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará en un plazo no superior a un año.");
		exerciseService.save(new Exercise(7,
				"1.3.7 Escoge el enunciado correcto para el asiento: \"10 211. Construcciones a 174. Provedores de inmovilizado a l/p 10\"",
				 texts, answer, lesson));

		//Unit 2 Lesson 1
		lesson = lessonService.findById(4);
		
		//Exercise 1
		answer = new Answer("uno");
		texts = Arrays.asList("213.Maquinaria", "210.Terrenos y bienes naturales", "218. Elementos de transporte");
		exercise =new Exercise(1, "2.1.1 Seleccione el asiento", texts, answer, lesson);
		//Save the images in the database
		imageService.saveImages(exercise, Paths.get("img/machine.jpg"),
				Paths.get("img/land.jpg"),
				Paths.get("img/truck.jpg"));
		
		//Exercise 2
		answer = new Answer("Este es un texto de prueba en el que comprobarlo");
		exerciseService.save(new Exercise(2, "2.1.2 Escribe la denominación de la cuenta que recoge: "
				+ "maquinarias para el proceso productivo de la empresa", null, answer, lesson));

		//Exercise 5
		answer = new Answer("uno");
		texts = Arrays.asList("Activo", "Pasivo", "Patrimonio neto");
		exerciseService.save(
				new Exercise(5, "2.1.5 Escoge la respuesta correcta para la cuenta: 210. Terrenos y bienes naturales",
						 texts, answer, lesson));

		//Exercise 7
		answer = new Answer("uno");
		texts = Arrays.asList(
				" La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará integramente dentro de 10 años a través de la letra de cambio.",
				"La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará integramente dentro de 10 años.",
				" La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará en un plazo no superior a un año.");
		exerciseService.save(new Exercise(7,
				"2.1.7 Escoge el enunciado correcto para el asiento: \"10 211. Construcciones a 174. Provedores de inmovilizado a l/p 10\"",
				 texts, answer, lesson));

		//Unit 2 Lesson 2
		lesson = lessonService.findById(5);
		
		//Exercise 1
		answer = new Answer("uno");
		texts = Arrays.asList("213.Maquinaria", "210.Terrenos y bienes naturales", "218. Elementos de transporte");
		exercise =new Exercise(1, "2.2.1 Seleccione el asiento", texts, answer, lesson);
		//Save the images in the database
		imageService.saveImages(exercise, Paths.get("img/machine.jpg"),
				Paths.get("img/land.jpg"),
				Paths.get("img/truck.jpg"));

		//Exercise 2
		answer = new Answer("Este es un texto de prueba en el que comprobarlo");
		exerciseService.save(new Exercise(2, "2.2.2 Escribe la denominación de la cuenta que recoge: "
				+ "maquinarias para el proceso productivo de la empresa", null, answer, lesson));


		//Exercise 5
		answer = new Answer("uno");
		texts = Arrays.asList("Activo", "Pasivo", "Patrimonio neto");
		exerciseService.save(
				new Exercise(5, "2.2.5 Escoge la respuesta correcta para la cuenta: 210. Terrenos y bienes naturales",
						 texts, answer, lesson));

		//Exercise 7
		answer = new Answer("uno");
		texts = Arrays.asList(
				" La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará integramente dentro de 10 años a través de la letra de cambio.",
				"La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará integramente dentro de 10 años.",
				" La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará en un plazo no superior a un año.");
		exerciseService.save(new Exercise(7,
				"2.2.7 Escoge el enunciado correcto para el asiento: \"10 211. Construcciones a 174. Provedores de inmovilizado a l/p 10\"",
				 texts, answer, lesson));

		//Unit 2 Lesson 3
		lesson = lessonService.findById(6);
		
		//Exercise 1
		answer = new Answer("uno");
		texts = Arrays.asList("213.Maquinaria", "210.Terrenos y bienes naturales", "218. Elementos de transporte");
		exercise = new Exercise(1, "2.3.1 Seleccione el asiento", texts, answer, lesson);
		// Save the images in the database
		imageService.saveImages(exercise, Paths.get("img/machine.jpg"),
				Paths.get("img/land.jpg"),
				Paths.get("img/truck.jpg"));
		
		//Exercise 2
		answer = new Answer("Este es un texto de prueba en el que comprobarlo");
		exerciseService.save(new Exercise(2, "2.3.2 Escribe la denominación de la cuenta que recoge: "
				+ "maquinarias para el proceso productivo de la empresa", null, answer, lesson));


		//Exercise 5
		answer = new Answer("uno");
		texts = Arrays.asList("Activo", "Pasivo", "Patrimonio neto");
		exerciseService.save(
				new Exercise(5, "2.3.5 Escoge la respuesta correcta para la cuenta: 210. Terrenos y bienes naturales",
						 texts, answer, lesson));

		//Exercise 7
		answer = new Answer("uno");
		texts = Arrays.asList(
				" La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará integramente dentro de 10 años a través de la letra de cambio.",
				"La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará integramente dentro de 10 años.",
				" La empresa compra un local por 10, dejándolo a deber a su provedor, al que pagará en un plazo no superior a un año.");
		exerciseService.save(new Exercise(7,
				"2.3.7 Escoge el enunciado correcto para el asiento: \"10 211. Construcciones a 174. Provedores de inmovilizado a l/p 10\"",
			  texts, answer, lesson));

	}
}
