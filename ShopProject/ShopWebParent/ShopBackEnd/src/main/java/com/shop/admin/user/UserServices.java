package com.shop.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

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

	public List<Role> listsRoles() {
		return (List<Role>) rRepo.findAll();
	}

	public void save(User user) {
		boolean isUpdatingUser = (user.getId() != null);
		if (isUpdatingUser) {
			User existingUser = urepo.findById(user.getId()).get();
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}

		urepo.save(user);
	}

	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = urepo.getUserByEmail(email);
		if (userByEmail == null)
			return true;
		boolean isCreatingNew = (id == null);
		if (isCreatingNew) {
			if (userByEmail != null)
				return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		return true;
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
			return urepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any use with ID" + id);
		}
	}

	public void delete(Integer id) throws UserNotFoundException {
		Long countById = urepo.countById(id);
		if(countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any use with ID" + id);
		}
		urepo.deleteById(id);
	}
}
