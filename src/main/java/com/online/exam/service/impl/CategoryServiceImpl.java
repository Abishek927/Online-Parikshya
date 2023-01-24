package com.online.exam.service.impl;

import com.online.exam.exception.ResourceNotFoundException;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.model.Category;
import com.online.exam.repo.FacultyRepo;
import com.online.exam.repo.RoleRepo;
import com.online.exam.repo.CategoryRepo;
import com.online.exam.repo.UserRepo;
import com.online.exam.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private FacultyRepo facultyRepo;

    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private QueryHelper queryHelper;

    @Override
     public Category createCategory(Long userId, Long facultyId, Category category) throws Exception {

        User user=this.queryHelper.getUserMethod(userId);
        Faculty faculty=this.queryHelper.getFacultyMethod(facultyId);
        Category retrievedCategory=this.categoryRepo.findByCategoryName(category.getCategoryName());

            List<UserFaculty> userFaculties=user.getUserFaculties();
            for (UserFaculty eachUserFaculty:userFaculties
                 ) {
                Faculty faculty1=eachUserFaculty.getFaculty();
                if(faculty1.getFacultyId().equals(faculty.getFacultyId())){
                    List<Category> categories=faculty1.getCategoryList();
                    for (Category eachCategory:categories
                         ) {
                        if(eachCategory.getCategoryName().equalsIgnoreCase(category.getCategoryName())){
                            throw new Exception("Category with the given name already exist in given faculty!!!!");
                        }else{



                            List<userRole> userRoles=user.getUserRoles();
                            for (userRole eachUserRole:userRoles
                            ) {
                                Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
                                if(role.getRoleName().equalsIgnoreCase("admin")){
                                    category.setFaculty(faculty);

                                    retrievedCategory=this.categoryRepo.save(category);



                                }

                            }



                        }

                    }


                }





        }

        return retrievedCategory;
    }

    @Override
    public String deleteCategory(Long userId, Long facultyId, String catName) throws Exception {
        String message="";
        User user=this.queryHelper.getUserMethod(userId);
        Faculty faculty=this.queryHelper.getFacultyMethod(facultyId);
        Category category=this.categoryRepo.findByCategoryName(catName);
        List<userRole> userRoles=user.getUserRoles();
        for (userRole eachUserRole:userRoles
             ) {
            Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
            if(role.getRoleName().equalsIgnoreCase("admin")){
                List<UserFaculty> userFaculties=user.getUserFaculties();
                if(userFaculties.isEmpty()){
                    throw new Exception("there is no faculty created by the given user!!!");
                }else{
                    for (UserFaculty eachUserFaculty:userFaculties
                         ) {
                        Faculty faculty1=eachUserFaculty.getFaculty();
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

            }
        }









        return message;
    }

    @Override
    public List<Category> readCategoryByFaculty(Long userId, Long facultyId) {

        List<Category> categories=new ArrayList<>();
        User user=queryHelper.getUserMethod(userId);
        Faculty faculty=queryHelper.getFacultyMethod(facultyId);


         List<userRole> userRoles=user.getUserRoles();
        for (userRole eachUserRole:userRoles
             ) {
            Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
            if(role.getRoleName().equalsIgnoreCase("admin")||role.getRoleName().equalsIgnoreCase("student")||role.getRoleName().equalsIgnoreCase("teacher")){
                 categories=faculty.getCategoryList();
            }


        }

        return categories;
    }

    @Override
    public Category readCategoryByName(Long userId,Long facultyId,String name) {
        User user=this.queryHelper.getUserMethod(userId);
        Faculty faculty=this.queryHelper.getFacultyMethod(facultyId);
        Category category=this.categoryRepo.findByCategoryName(name);
        List<Category> categories=faculty.getCategoryList();
        for (Category eachCategory:categories
             ) {
            if(eachCategory.getCategoryId().equals(category.getCategoryId())){
                List<userRole> userRoles=user.getUserRoles();
                for (userRole eachUserRole:userRoles
                     ) {
                    Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
                    if(role.getRoleName().equalsIgnoreCase("admin")||role.getRoleName().equalsIgnoreCase("student")||role.getRoleName().equalsIgnoreCase("teacher")){
                        return category;
                    }
                }

            }
        }

        return null;
    }


}
