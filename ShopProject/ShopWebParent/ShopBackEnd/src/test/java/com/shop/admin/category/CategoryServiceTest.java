package com.shop.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shop.common.entities.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {
	@MockBean
	private CategoryRepository repo;

	@InjectMocks
	private CategoryService service;

	@Test
	public void testCheckUniqueInNewModeReturnDublicateName() {
		Integer id = null;
		String name = "Computers";
		String alias = "abc";
		Category category = new Category(id, name, alias);
		Mockito.when(repo.findByName(name)).thenReturn(category);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("DublicateName");

	}
	
	@Test
	public void testCheckUniqueInNewModeReturnDublicateAlias() {
		Integer id = null;
		String name = "ABC";
		String alias = "computers";
		Category category = new Category(id, name, alias);
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("DublicateAlias");

	}
	
	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		Integer id = null;
		String name = "Name ABC";
		String alias = "computers";

		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("OK");

	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDublicateName() {
		Integer id = 1;
		String name = "Computers";
		String alias = "abc";
		Category category = new Category(2, name, alias);
		Mockito.when(repo.findByName(name)).thenReturn(category);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("DublicateName");

	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDublicateAlias() {
		Integer id = 1;
		String name = "ABC";
		String alias = "computers";
		Category category = new Category(2, name, alias);
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("DublicateAlias");

	}
	
	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		Integer id = 1;
		String name = "Name ABC";
		String alias = "computers";

		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("OK");

	}
}
