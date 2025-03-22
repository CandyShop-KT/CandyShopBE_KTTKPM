package com.example.demo.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CategoryRequestDTO;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;

@Service
public class CategoryServiceImp implements CategoryService{
	
	private CategoryRepository categoryRepository;
	
	public CategoryServiceImp(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	@Transactional
	public Category createCategory(CategoryRequestDTO categoryName) {
		if (categoryRepository.existsByCategoryNameIgnoreCase(categoryName.getCategoryName()))
			throw new ResourceConflictException("categoryName", "Category name already exists");
		Category category = new Category();
		category.setCategoryName(categoryName.getCategoryName());
		return categoryRepository.save(category);
	}

	@Override
	@Transactional
	public Category updateCategory(String categoryId, CategoryRequestDTO categoryName) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		if (categoryRepository.existsByCategoryNameIgnoreCase(categoryName.getCategoryName()))
			throw new ResourceConflictException("categoryName", "Category name already exists");
		if (category.getCategoryName().equals(categoryName.getCategoryName()))
			throw new ResourceConflictException("categoryName", "Category name is the same");
		category.setCategoryName(categoryName.getCategoryName());
		return categoryRepository.save(category);
	}

	@Override
	@Transactional
	public void deleteCategory(String categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		categoryRepository.delete(category);
	}

	@Override
	public Category getCategory(String categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

}
