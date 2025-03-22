package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.CategoryRequestDTO;
import com.example.demo.model.Category;

public interface CategoryService {
	
	public Category createCategory(CategoryRequestDTO categoryName);
	
	public Category updateCategory(String categoryId, CategoryRequestDTO categoryName);
	
	public void deleteCategory(String categoryId);
	
	public Category getCategory(String categoryId);
	
	public List<Category> getAllCategories();
	
}
