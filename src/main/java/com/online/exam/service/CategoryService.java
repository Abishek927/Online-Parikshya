package com.online.exam.service;

import com.online.exam.dto.CategoryDto;
import com.online.exam.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(Long userId, Long facultyId, CategoryDto categoryDto) throws Exception;
    CategoryDto updateCategory(Long userId,Long facId,Long catId,CategoryDto categoryDto) throws Exception;


    String deleteCategory(Long userId,Long facultyId,String catName) throws Exception;

    List<CategoryDto> readCategoryByFaculty(Long userId, Long facultyId);

    CategoryDto readCategoryByName(Long userId,Long facultyId,String name);


}
