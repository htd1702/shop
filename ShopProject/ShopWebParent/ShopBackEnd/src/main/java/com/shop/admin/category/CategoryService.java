package com.shop.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.common.entities.Category;

@Service
@Transactional
public class CategoryService {

	public static final int ROOT_CATEGORIES_PER_PAGE = 4;

	@Autowired
	private CategoryRepository catRepo;

	public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir, String keyword) {
		Sort sort = Sort.by("name");

		if (sortDir.equals("asc")) {
			sort = sort.ascending();
		} else if (sortDir.equals("desc")) {
			sort = sort.descending();
		}

		Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sort);

		Page<Category> pageCategories = null;
		
		if (keyword != null && !keyword.isEmpty()) {
			pageCategories = catRepo.search(keyword, pageable);
		} else {
			pageCategories = catRepo.findRootCategory(pageable);
		}

		List<Category> rootCategories = pageCategories.getContent();

		pageInfo.setTotalElements(pageCategories.getTotalElements());
		pageInfo.setTotalPages(pageCategories.getTotalPages());


		if (keyword != null && !keyword.isEmpty()) {
			List<Category> searchResult = pageCategories.getContent();
			for(Category category: searchResult) {
				category.setHasChildren(category.getChildren().size()>0);
			}
			return searchResult;
		}else {
			return listHierarchicalCategories(rootCategories, sortDir);
		}
		
	}

	private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
		List<Category> hierarchicalCategories = new ArrayList<>();
		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.CopyFull(rootCategory));

			Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.CopyFull(subCategory, name));
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
			}
		}
		return hierarchicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel,
			String sortDir) {
		Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
		int newSubLevel = subLevel + 1;
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}

			name += subCategory.getName();
			hierarchicalCategories.add(Category.CopyFull(subCategory, name));
			listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
		}
	}

	public Category save(Category category) {
		return catRepo.save(category);
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoryUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = catRepo.findRootCategory(Sort.by("name").ascending());
		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoryUsedInForm.add(Category.copyIdAndName(category));
				Set<Category> children = sortSubCategories(category.getChildren());
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
		Set<Category> children = sortSubCategories(parent.getChildren());
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

	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNew = (id == null || id == 0);
		Category categoryByName = catRepo.findByName(name);
		if (isCreatingNew) {
			if (categoryByName != null) {
				return "DublicateName";
			} else {
				Category categoryByAlias = catRepo.findByAlias(alias);
				if (categoryByAlias != null) {
					return "DublicateAlias";
				}
			}
		} else {
			if (categoryByName != null && categoryByName.getId() != id) {
				return "DublicateName";
			}

			Category categoryByAlias = catRepo.findByAlias(alias);
			if (categoryByAlias != null && categoryByAlias.getId() != id) {
				return "DublicateAlias";
			}
		}
		return "OK";
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children) {
		return sortSubCategories(children, "asc");
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
			@Override
			public int compare(Category cat1, Category cat2) {
				if (sortDir.equals("asc")) {
					return cat1.getName().compareTo(cat2.getName());
				} else {
					return cat2.getName().compareTo(cat1.getName());
				}
			}
		});
		sortedChildren.addAll(children);
		return sortedChildren;
	}

	public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
		catRepo.updateEnabledStatus(id, enabled);
	}

	public void delete(Integer id) throws CategoryNotFoundException {
		Long countById = catRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}
		catRepo.deleteById(id);
	}
}
