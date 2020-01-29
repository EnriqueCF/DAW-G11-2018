package com.daw.contafin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.daw.contafin.model.User;
import com.daw.contafin.service.ExcelService;
import com.daw.contafin.service.UserService;
import com.daw.contafin.user.UserComponent;


@Controller
@RequestMapping ("Admin")
public class AdminController  {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserComponent userComponent;
	
	@Autowired
	ExcelService excelService;
	
	
	@RequestMapping("UsersData")
	public String usersData(Model model) {
		model.addAttribute("users",userService.getUsers().subList(0,Math.min(userService.getUsers().size(), 10)));
		return "userData";
	}
	
	@RequestMapping ("ExportPreview")
	public ModelAndView userData (ModelAndView model) {
		 List<User> users = userService.getUsers();
		return new ModelAndView("exportPreview", "userList", users);
	}
	 
	 @RequestMapping(value="Export", method=RequestMethod.GET)
	 public void userListReport(HttpServletResponse response) throws IOException{
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=\"user_list.xls\"");
		Workbook workbook = excelService.generateExcel();
		workbook.write(response.getOutputStream());
	 }
	 
	 //Load users using AJAX
	
	@RequestMapping(value={"loadUsers/{page}"})
	public String indexScrollPosts(Model model, @PathVariable int page){
		
		List<User> users =userService.getUsers();
		List<User> addUsers = null;
		if (page*10 < users.size()){
			if ((page+1)*10 <= users.size()){
				addUsers = users.subList(page*10, (page+1)*10);
			} else {
				addUsers = users.subList(page*10,users.size());
			}
		}
		model.addAttribute("users",addUsers);
		return "loadUsers";
	}

	 
	
}
	 
