package com.online.exam.service;

import com.online.exam.dto.CategoryDto;


import java.security.Principal;
import java.util.List;


public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto, Principal principal) throws Exception;


    CategoryDto updateCategory(CategoryDto categoryDto,Principal principal) throws Exception;


    String deleteCategory(Long catId,Principal principal) throws Exception;

    List<CategoryDto> readCategoryByFaculty(Long facultyId) throws Exception;

    CategoryDto readCategoryById(Long catId);
    List<CategoryDto> readAllCategory(Principal principal);
    Integer countCategory(Principal principal);


}
