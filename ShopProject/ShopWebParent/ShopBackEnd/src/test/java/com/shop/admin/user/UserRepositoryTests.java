package com.shop.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entities.Role;
import com.shop.common.entities.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userDatHT = new User("htd1702@gmail.com", "nam2021", "Dat", "Ho Tien");
		userDatHT.addRole(roleAdmin);
		User saveUser = repo.save(userDatHT);
		assertThat(saveUser.getId()).isGreaterThan(0);

	}

	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userRavi = new User("ravi@gmail.com", "ravi2021", "rai", "Res");
		Role roleEditor = new Role(3);
		Role roleAssitant = new Role(5);
		userRavi.addRole(roleEditor);
		userRavi.addRole(roleAssitant);
		User saveUser = repo.save(userRavi);
		assertThat(saveUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));

	}

	@Test
	public void testGetUserById() {
		User userDat = repo.findById(1).get();
		System.out.println(userDat);
		assertThat(userDat).isNotNull();

	}

	@Test
	public void testUpdateUserDetails() {
		User userDat = repo.findById(1).get();
		userDat.setEnabled(true);
		userDat.setEmail("dat@gmail.com");
		repo.save(userDat);

	}

	@Test
	public void testUpdateUserRole() {
		User userRavi = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesPerson = new Role(2);

		userRavi.getRoles().remove(roleEditor);
		userRavi.addRole(roleSalesPerson);
		repo.save(userRavi);

	}

	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}

	@Test
	public void testGetUserByEmail() {
		String email = "htd1702@gmail.com";
		User user = repo.getUserByEmail(email);
		assertThat(user).isNotNull();
	}

	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}

	@Test
	public void testDisabledUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
	}

	@Test
	public void testEnabledUser() {
		Integer id = 3;
		repo.updateEnabledStatus(id, true);
	}

	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		List<User> listUser = page.getContent();
		
		listUser.forEach(user->System.out.println(user));
		assertThat(listUser.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUsers() {
		String keyword ="bruce";
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword,pageable);
		List<User> listUser = page.getContent();
		
		listUser.forEach(user->System.out.println(user));
		assertThat(listUser.size()).isGreaterThan(0);
	}

}
