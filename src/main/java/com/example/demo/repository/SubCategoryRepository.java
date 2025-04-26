package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.SubCategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, String> {
	boolean existsBySubCategoryNameIgnoreCase(String subCategoryName);

	@Query("SELECT s FROM SubCategory s WHERE LOWER(s.subCategoryName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<SubCategory> searchSubCategories(@Param("keyword") String keyword);

	@Query("SELECT s FROM SubCategory s WHERE s.category.categoryId = :categoryId")
	List<SubCategory> findByCategoryId(@Param("categoryId") String categoryId);
}
