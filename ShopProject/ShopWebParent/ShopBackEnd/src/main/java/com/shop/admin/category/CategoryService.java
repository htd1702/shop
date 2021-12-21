package com.shop.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.catalog.CatalogException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entities.Category;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository catRepo;

	public List<Category> listAll() {
		List<Category> rootCategories = catRepo.findRootCategory();
		return listHierarchicalCategories(rootCategories);
	}

	private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
		List<Category> hierarchicalCategories = new ArrayList<>();
		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.CopyFull(rootCategory));

			Set<Category> children = rootCategory.getChildren();

			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.CopyFull(subCategory, name));
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
			}
		}
		return hierarchicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel) {
		Set<Category> children = parent.getChildren();
		int newSubLevel = subLevel + 1;
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}

			name += subCategory.getName();
			hierarchicalCategories.add(Category.CopyFull(subCategory, name));
			listSubCategoriesUsedInForm(hierarchicalCategories, subCategory, newSubLevel);
		}
	}

	public Category save(Category category) {
		return catRepo.save(category);
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoryUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = catRepo.findAll();
		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoryUsedInForm.add(Category.copyIdAndName(category));
				Set<Category> children = category.getChildren();
				for (Category subCategory : children) {
					String name = "--" + subCategory.getName();
					categoryUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
					listSubCategoriesUsedInForm(categoryUsedInForm, subCategory, 1);
				}
			}
		}
		return categoryUsedInForm;
	}

	private void listSubCategoriesUsedInForm(List<Category> categoryUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			categoryUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
			listSubCategoriesUsedInForm(categoryUsedInForm, subCategory, newSubLevel);
		}
	}

	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return catRepo.findById(id).get();
		} catch (Exception e) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}
	}
}
