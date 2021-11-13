package com.shop.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.common.entities.Role;
import com.shop.common.entities.User;

@Controller
public class UserController {

	@Autowired
	private UserServices services;

	@GetMapping("/users")
	public String listAll(Model model) {

		List<User> listUsers = services.listAlls();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}

	@GetMapping("users/new")
	public String newUser(Model model) {
		List<Role> listRoles = services.listsRoles();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);

		return "user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user,RedirectAttributes redirectAttributes) {
		System.out.println(user);
		services.save(user);	
		redirectAttributes.addFlashAttribute("message","The user has been save succesfully.");
		return "redirect:/users";
	}
	
	
}
