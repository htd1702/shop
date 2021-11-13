package com.shop.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shop.common.entities.Role;
import com.shop.common.entities.User;

@Service
public class UserServices {
	
	@Autowired
	private UserRepository urepo;

	@Autowired
	private RoleRepository rRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public List<User> listAlls() {
		return (List<User>) urepo.findAll();
	}
	
	public List<Role> listsRoles(){
		return (List<Role>)rRepo.findAll();
	}
	
	public void save(User user) {
		encodePassword(user);
		urepo.save(user);
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public boolean isEmailUnique(String email) {
		User userByEmail = urepo.getUserByEmail(email);
		return userByEmail == null;
	}
}
