package com.online.exam.service.impl;

import com.online.exam.dto.CategoryDto;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.model.Category;
import com.online.exam.repo.FacultyRepo;
import com.online.exam.repo.RoleRepo;
import com.online.exam.repo.CategoryRepo;
import com.online.exam.repo.UserRepo;
import com.online.exam.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

        User user=this.queryHelper.getUserMethod(userId);
        Faculty faculty=this.queryHelper.getFacultyMethod(facultyId);
        Category retrievedCategory=this.categoryRepo.findByCategoryName(categoryDto.getCategoryName());

            Set<Faculty> Faculties=user.getFaculties();
            for (Faculty eachFaculty:Faculties
                 ) {
                Faculty faculty1=eachFaculty;
                if(faculty1.getFacultyId().equals(faculty.getFacultyId())){
                    List<Category> categories=faculty1.getCategoryList();
                    for (Category eachCategory:categories
                         ) {
                        if(eachCategory.getCategoryName().equalsIgnoreCase(categoryDto.getCategoryName())){
                            throw new Exception("Category with the given name already exist in given faculty!!!!");
                        }else{



                            Category category=this.modelMapper.map(categoryDto,Category.class);


                                    category.setFaculty(faculty);

                                    retrievedCategory=this.categoryRepo.save(category);









                        }

                    }


                }





        }

        return this.modelMapper.map(retrievedCategory,CategoryDto.class);
    }

    @Override
    public String deleteCategory(Long userId, Long facultyId, String catName) throws Exception {
        String message="";
        User user=this.queryHelper.getUserMethod(userId);
        Faculty faculty=this.queryHelper.getFacultyMethod(facultyId);
        Category category=this.categoryRepo.findByCategoryName(catName);

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
                                    for (Course eachCourse:courses
                                         ) {
                                        eachCourse.setCategory(null);
                                    }
                                    List<User> users=category.getUsers();
                                    for (User eachUser:users
                                         ) {
                                        eachUser.setCategory(null);

                                    }
                                    category.setFaculty(null);
                                    this.categoryRepo.deleteById(category.getCategoryId());
                                    message="category deleted successfully";
                                }
                            }
                        }

                    }
                }












        return message;
    }

    @Override
    public List<CategoryDto> readCategoryByFaculty(Long userId, Long facultyId) {

        List<Category> categories=new ArrayList<>();
        User user=queryHelper.getUserMethod(userId);
        Faculty faculty=queryHelper.getFacultyMethod(facultyId);
        categories=faculty.getCategoryList();
        List<CategoryDto>categoryDtos=categories.stream().map(category -> this.modelMapper.map(category,CategoryDto.class)).collect(Collectors.toList());





        return categoryDtos;
    }

    @Override
    public CategoryDto readCategoryByName(Long userId,Long facultyId,String name) {
        User user=this.queryHelper.getUserMethod(userId);
        Faculty faculty=this.queryHelper.getFacultyMethod(facultyId);
        Category category=this.categoryRepo.findByCategoryName(name);
        List<Category> categories=faculty.getCategoryList();
        for (Category eachCategory:categories
             ) {
            if(eachCategory.getCategoryId().equals(category.getCategoryId())){


                        return this.modelMapper.map(category,CategoryDto.class);



            }
        }

        return null;
    }


}
