package com.shop.admin.category;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entities.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

}
