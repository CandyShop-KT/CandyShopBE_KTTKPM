package com.example.demo.service.imp;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.SubCategoryRequestDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.SubCategory;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.SubCategoryRepository;
import com.example.demo.service.SubCategoryService;

@Repository
public class SubCategoryServiceImp implements SubCategoryService {
	
	private CategoryRepository categoryRepository;
	private SubCategoryRepository subCategoryRepository;
	
	public SubCategoryServiceImp(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository) {
		this.categoryRepository = categoryRepository;
		this.subCategoryRepository = subCategoryRepository;
	}

	@Override
	@Transactional
	public SubCategory createSubCategory(String categoryId, SubCategoryRequestDTO subCategoryRequestDTO) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		if (subCategoryRepository.existsBySubCategoryNameIgnoreCase(subCategoryRequestDTO.getSubCategoryName())) 
			throw new RuntimeException("Sub Category name already exists");
		SubCategory subCategory = new SubCategory();
		subCategory.setSubCategoryName(subCategoryRequestDTO.getSubCategoryName());
		subCategory.setCategory(category);
		return subCategoryRepository.save(subCategory);
	}

	@Override
	@Transactional
	public SubCategory updateSubCategory(String subCategoryId, SubCategoryRequestDTO subCategoryRequestDTO) {
		SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Sub Category not found"));
		if (subCategoryRepository.existsBySubCategoryNameIgnoreCase(subCategoryRequestDTO.getSubCategoryName()))
			throw new RuntimeException("Sub Category name already exists");
		subCategory.setSubCategoryName(subCategoryRequestDTO.getSubCategoryName());
		return subCategoryRepository.save(subCategory);
	}

	@Override
	public SubCategory getSubCategory(String subCategoryId) {
		return subCategoryRepository.findById(subCategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Sub Category not found"));
	}

	@Override
	@Transactional
	public void deleteSubCategory(String subCategoryId) {
		SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Sub Category not found"));
		subCategoryRepository.delete(subCategory);
	}

	@Override
	public List<SubCategory> getAllSubCategories() {
		return subCategoryRepository.findAll();
	}

}
