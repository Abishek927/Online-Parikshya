package com.online.exam.helper;

import com.online.exam.exception.ResourceNotFoundException;
import com.online.exam.model.*;
import com.online.exam.repo.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component

public class  QueryHelper {
    @Autowired
    private  CategoryRepo categoryRepo;

    @Autowired
    private  FacultyRepo facultyRepo;
    @Autowired
    private  RoleRepo roleRepo;
    @Autowired
    private  UserRepo userRepo;
    @Autowired
    private CourseRepo courseRepo;

    public    Category getCategoryMethod(Long catId){
        return this.categoryRepo.findById(catId).orElseThrow(()->new ResourceNotFoundException("category","categoryId "+catId,null));
    }

    public Faculty getFacultyMethod(Long facultyId){
        return this.facultyRepo.findById(facultyId).orElseThrow(()->new ResourceNotFoundException("faculty","facultyId "+facultyId,null));
    }

    public User getUserMethod(Long userId){
        return this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","userId "+userId,null));
    }

    public Role getRoleMethod(Long roleId){
        return this.roleRepo.findById(roleId).orElseThrow(()->new ResourceNotFoundException("role","roleId "+roleId,null));
    }

    public Course getCourseMethod(Long courseId){
        return this.courseRepo.findById(courseId).orElseThrow(()->new ResourceNotFoundException("course","courseId "+courseId,null));
    }
}
