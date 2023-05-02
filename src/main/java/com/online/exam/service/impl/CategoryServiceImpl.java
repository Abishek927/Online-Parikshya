package com.online.exam.service.impl;


import com.online.exam.dto.CategoryDto;
import com.online.exam.dto.CourseDto;
import com.online.exam.dto.FacultyDto;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.model.Category;

import com.online.exam.repo.RoleRepo;
import com.online.exam.repo.CategoryRepo;

import com.online.exam.repo.UserRepo;
import com.online.exam.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private QueryHelper queryHelper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepo userRepo;



    @Override
     public CategoryDto createCategory(CategoryDto categoryDto, Principal principal) throws Exception {
        User user=this.queryHelper.getUserMethod(categoryDto.getUserId());
        Faculty faculty=this.queryHelper.getFacultyMethod(categoryDto.getFacultyId());
        Set<Faculty> Faculties = user.getFaculties();
    for (Faculty eachFaculty : Faculties
    ) {
        Faculty faculty1 = eachFaculty;
        if (faculty1.getFacultyId().equals(faculty.getFacultyId())) {
            List<Category> categories = faculty1.getCategoryList();
            for (Category eachCategory : categories
            ) {
                if(eachCategory.getCategoryName().equals(categoryDto.getCategoryName())){
                    throw new Exception("category with the given name already exist in the given faculty with id "+categoryDto.getFacultyId());
                }
            }
            Category category = new Category();
            category.setCategoryName(categoryDto.getCategoryName());
            category.setFaculty(faculty);
            category.setUsers(Set.of(user));
            user.getCategories().add(category);
            category = this.categoryRepo.save(category);
           categoryDto.setCategoryId(category.getCategoryId());
        }
    }
    return categoryDto;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto,Principal principal) throws Exception {
        User retrievedUser=userRepo.findByUserEmail(principal.getName());
        Category retrievedCategory=this.queryHelper.getCategoryMethod(categoryDto.getCategoryId());
       if(retrievedCategory.getUsers().contains(retrievedUser)) {
           if (categoryDto.getCategoryName() != null) {
               retrievedCategory.setCategoryName(categoryDto.getCategoryName());
           }
           if ((categoryDto.getFacultyId() != retrievedCategory.getFaculty().getFacultyId()) && (categoryDto.getFacultyId() != null)) {
               Faculty retrievedFaculty = queryHelper.getFacultyMethod(categoryDto.getFacultyId());
               if(retrievedFaculty.getUsers().contains(retrievedUser)){
                   retrievedCategory.setFaculty(retrievedFaculty);
               }
           }
           Category resultCategory = this.categoryRepo.save(retrievedCategory);


           return categoryDto;
       }


        return null;
    }

    @Override
    public String deleteCategory(Long catId,Principal principal)  {
        String message="";
        User user=userRepo.findByUserEmail(principal.getName());
        Category category=this.queryHelper.getCategoryMethod(catId);

        List<Course> courses=category.getCourseList();
        if(!courses.isEmpty()){
            for (Course eachCourse:courses)
            {
                eachCourse.setCategory(null);
            }
        }
        category.setUsers(null);
        user.getCategories().remove(category);
        category.setFaculty(null);
        this.categoryRepo.deleteById(catId);
        message="category deleted successfully";
        return message;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> readCategoryByFaculty(Long facultyId){
           Faculty retrievedFaculty=queryHelper.getFacultyMethod(facultyId);
               List<Category> categories=categoryRepo.findByFaculty(retrievedFaculty);
               if(!categories.isEmpty()){
                   return categories.stream().map(category -> this.modelMapper.map(category,CategoryDto.class)).collect(Collectors.toList());
               }

        return null;
    }
    @Transactional(readOnly = true)
    @Override
    public CategoryDto readCategoryById(Long catId) {
        Category retrievedCategory=queryHelper.getCategoryMethod(catId);
        return modelMapper.map(retrievedCategory,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> readAllCategory(Principal principal) {
        User retrievedUser=userRepo.findByUserEmail(principal.getName());
        List<Category> categories=categoryRepo.findAll();
        List<CategoryDto> categoryDtos=new ArrayList<>();
        if(!categories.isEmpty()){
            for (Category eachCategory:categories
                 ) {
                if(eachCategory.getUsers().contains(retrievedUser)){
                    categoryDtos.add(this.modelMapper.map(eachCategory,CategoryDto.class));
                }
            }
            return categoryDtos;
        }

        return null;
    }


}
