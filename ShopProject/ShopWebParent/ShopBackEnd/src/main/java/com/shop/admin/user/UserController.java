package com.shop.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		model.addAttribute("pageTitle", "Create New User");
		return "user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes) {
		System.out.println(user);
		services.save(user);
		redirectAttributes.addFlashAttribute("message", "The user has been save succesfully.");
		return "redirect:/users";
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = services.get(id);
			List<Role> listRoles = services.listsRoles();
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User (ID:" + id + ")");
			model.addAttribute("listRoles", listRoles);
			return "user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}

	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			services.delete(id);
			redirectAttributes.addFlashAttribute("message", "The user ID" + id + " has been deleted successfully.");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/users";
	}

}
