package com.online.exam.service;

import com.online.exam.dto.CategoryDto;


import java.util.List;


public interface CategoryService {

    CategoryDto createCategory(Long userId, Long facultyId, CategoryDto categoryDto) throws Exception;


    CategoryDto updateCategory(Long userId,Long facId,Long catId,CategoryDto categoryDto) throws Exception;


    String deleteCategory(Long userId,Long facultyId,Long catId) throws Exception;

    List<CategoryDto> readCategoryByFaculty(Long userId, Long facultyId) throws Exception;

    CategoryDto readCategoryByName(Long userId,Long facultyId,Long catId);


}
