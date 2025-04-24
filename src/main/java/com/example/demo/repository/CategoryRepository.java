package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
	boolean existsByCategoryNameIgnoreCase(String categoryName);

	@Query("SELECT c FROM Category c WHERE LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Category> searchCategories(@Param("keyword") String keyword);

	@Query("SELECT c FROM Category c WHERE c.categoryId IN (SELECT s.category.categoryId FROM SubCategory s WHERE LOWER(s.subCategoryName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	List<Category> searchCategoriesBySubCategoryName(@Param("keyword") String keyword);
}
