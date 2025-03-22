package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.SubCategoryRequestDTO;
import com.example.demo.model.SubCategory;

public interface SubCategoryService {
	
	public SubCategory createSubCategory(String categoryId, SubCategoryRequestDTO subCategoryRequestDTO);

	public SubCategory updateSubCategory(String subCategoryId, SubCategoryRequestDTO subCategoryRequestDTO);

	public SubCategory getSubCategory(String subCategoryId);

	public void deleteSubCategory(String subCategoryId);

	public List<SubCategory> getAllSubCategories();
	
}
