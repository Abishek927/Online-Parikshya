package com.online.exam.service.impl;


import com.online.exam.dto.CategoryDto;
import com.online.exam.dto.CourseDto;
import com.online.exam.dto.FacultyDto;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.model.Category;

import com.online.exam.repo.RoleRepo;
import com.online.exam.repo.CategoryRepo;

import com.online.exam.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private RoleRepo roleRepo;


    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private QueryHelper queryHelper;
    @Autowired
    private ModelMapper modelMapper;

    @Override
     public CategoryDto createCategory(Long userId, Long facultyId, CategoryDto categoryDto) throws Exception {
        CategoryDto categoryDto1=new CategoryDto();

        User user=this.queryHelper.getUserMethod(userId);
        Faculty faculty=this.queryHelper.getFacultyMethod(facultyId);
        Category resultCategory=new Category();

    Set<Faculty> Faculties = user.getFaculties();
    for (Faculty eachFaculty : Faculties
    ) {
        Faculty faculty1 = eachFaculty;
        if (faculty1.getFacultyId().equals(faculty.getFacultyId())) {
            List<Category> categories = faculty1.getCategoryList();
            for (Category eachCategory : categories
            ) {
                if(eachCategory.getCategoryName().equals(categoryDto.getCategoryName())){
                    throw new Exception("category with the given name already exist in the given faculty with id "+facultyId);
                }
            }
            Category category = new Category();
            category.setCategoryName(categoryDto.getCategoryName());


            category.setFaculty(faculty);
            category.setUsers(Set.of(user));
            user.getCategories().add(category);

            resultCategory = this.categoryRepo.save(category);
            categoryDto1.setCategoryId(resultCategory.getCategoryId());
            categoryDto1.setCategoryName(resultCategory.getCategoryName());


        }
    }


        return categoryDto1;
    }

    @Override
    public CategoryDto updateCategory(Long userId, Long facId, Long catId, CategoryDto categoryDto) throws Exception {
        CategoryDto  categoryDto1=new CategoryDto();
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty=this.queryHelper.getFacultyMethod(facId);
        Category retrievedCategory=this.queryHelper.getCategoryMethod(catId);
        Set<Faculty> faculties=retrievedUser.getFaculties();
        if(!faculties.isEmpty()){
            for (Faculty eachFaculty:faculties
                 ) {
                if(eachFaculty.getFacultyId().equals(retrievedFaculty.getFacultyId())){
                    List<Category> categories=eachFaculty.getCategoryList();
                    if(!categories.isEmpty()){
                        for (Category eachCategory:categories
                             ) {
                            if(eachCategory.getCategoryId().equals(catId)){
                                if(categoryDto.getCategoryName()!=null){
                                    retrievedCategory.setCategoryName(categoryDto.getCategoryName());
                                }
                                if(categoryDto.getFacultyDto()!=null){
                                    FacultyDto facultyDto =categoryDto.getFacultyDto();
                                    Faculty resultFaculty=this.modelMapper.map(facultyDto,Faculty.class);
                                    for (Faculty eachFaculty1:faculties
                                         ) {
                                        if(eachFaculty1.getFacultyName().equals(facultyDto.getFacultyName())){
                                            retrievedCategory.setFaculty(resultFaculty);
                                        }

                                    }



                                }
                                Category resultCategory=this.categoryRepo.save(retrievedCategory);
                                Faculty faculty=resultCategory.getFaculty();
                                FacultyDto facultyDto=this.modelMapper.map(faculty,FacultyDto.class);
                                categoryDto1.setCategoryName(resultCategory.getCategoryName());
                                categoryDto1.setCategoryId(resultCategory.getCategoryId());
                                categoryDto1.setFacultyDto(facultyDto);
                                return categoryDto1;

                            }
                        }

                    }else {
                        throw new Exception("there is no category for the given faculty with id "+eachFaculty.getFacultyId());
                    }
                }
            }

        }else {
            throw new Exception("there is no faculties for the given user!!!");
        }

        return null;
    }

    @Override
    public String deleteCategory(Long userId, Long facultyId,Long catId) throws Exception {
        String message="";
        User user=this.queryHelper.getUserMethod(userId);
        Faculty faculty=this.queryHelper.getFacultyMethod(facultyId);
        Category category=this.queryHelper.getCategoryMethod(catId);
        if(category!=null){

                Set<Faculty> userFaculties=user.getFaculties();
                if(userFaculties.isEmpty()){
                    throw new Exception("there is no faculty created by the given user!!!");
                }else{
                    for (Faculty eachUserFaculty:userFaculties
                         ) {
                        Faculty faculty1=eachUserFaculty;
                        if(faculty1.getFacultyId().equals(faculty.getFacultyId())){
                            List<Category> categories=faculty1.getCategoryList();
                            for (Category eachCategory:categories
                                 ) {
                                if(eachCategory.getCategoryId().equals(category.getCategoryId())){
                                    List<Course> courses=category.getCourseList();
                                    if(!courses.isEmpty()){
                                        for (Course eachCourse:courses
                                             ) {
                                            eachCourse.setCategory(null);
                                        }

                                    }
                                    category.setUsers(null);
                                    user.getCategories().remove(category);

                                    category.setFaculty(null);

                                    this.categoryRepo.deleteById(catId);
                                    message="category deleted successfully";
                                }
                            }
                        }

                    }
                }
        }else {
            return message="there is no category with the given id "+catId;
        }












        return message;
    }

    @Override
    public List<CategoryDto> readCategoryByFaculty(Long userId, Long facultyId) throws Exception {


        List<Category> categories=new ArrayList<>();
        User user=queryHelper.getUserMethod(userId);
        Faculty faculty=queryHelper.getFacultyMethod(facultyId);
        categories=faculty.getCategoryList();
        Set<Faculty> faculties=user.getFaculties();
        List<CategoryDto> categoryDtos=new ArrayList<>(faculties.size());
        if(!faculties.isEmpty()) {

            for (Faculty eachFaculty:faculties
                 ) {
                if (eachFaculty.getFacultyId().equals(facultyId)) {
                    if (!categories.isEmpty()) {

                        for (Category eachCat:categories
                             ) {
                            CategoryDto categoryDto=new CategoryDto();

                            categoryDto.setCategoryId(eachCat.getCategoryId());
                            categoryDto.setCategoryName(eachCat.getCategoryName());

                            categoryDto.setFacultyDto(this.modelMapper.map(eachCat.getFaculty(),FacultyDto.class));
                            List<Course> courses=eachCat.getCourseList();
                            List<CourseDto> courseDtos=courses.stream().map(course -> this.modelMapper.map(course, CourseDto.class)).collect(Collectors.toList());
                            categoryDto.setCourseDtos(courseDtos);

                                categoryDtos.add(categoryDto);



                        }







                    } else {
                        return null;
                    }


                }
            }
        }else {
            throw new Exception("there is no faculties for the given user with id "+userId);
        }





        return categoryDtos;
    }

    @Override
    public CategoryDto readCategoryById(Long userId,Long facultyId,Long catId) {
        CategoryDto categoryDto=new CategoryDto();
        User user=this.queryHelper.getUserMethod(userId);
        Faculty faculty=this.queryHelper.getFacultyMethod(facultyId);
        Category category=this.queryHelper.getCategoryMethod(catId);
        List<Category> categories=faculty.getCategoryList();

        Set<Faculty> faculties=user.getFaculties();
        if(!faculties.isEmpty()) {
            for (Faculty eachFaculty:faculties
                 ) {

                if (eachFaculty.getFacultyId().equals(facultyId)) {
                    for (Category eachCategory : categories
                    ) {
                        if (eachCategory.getCategoryId().equals(category.getCategoryId())) {
                            categoryDto.setCategoryId(category.getCategoryId());
                            categoryDto.setCategoryName(category.getCategoryName());
                            categoryDto.setFacultyDto(this.modelMapper.map(category.getFaculty(),FacultyDto.class));
                            List<Course> courses=category.getCourseList();
                            if(!courses.isEmpty()){
                                categoryDto.setCourseDtos(courses.stream().map(course -> this.modelMapper.map(course,CourseDto.class)).collect(Collectors.toList()));
                            }


                            return categoryDto;


                        }
                    }
                }
            }
        }

        return null;
    }


}
