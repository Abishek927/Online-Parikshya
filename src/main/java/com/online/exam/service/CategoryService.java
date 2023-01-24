package com.online.exam.service;

import com.online.exam.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Long userId, Long facultyId, Category category) throws Exception;


    String deleteCategory(Long userId,Long facultyId,String catName) throws Exception;

    List<Category> readCategoryByFaculty(Long userId, Long facultyId);

    Category readCategoryByName(Long userId,Long facultyId,String name);


}
