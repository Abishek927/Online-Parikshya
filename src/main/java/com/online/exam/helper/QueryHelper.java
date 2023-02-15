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
    @Autowired
    private ExamRepo examRepo;
    @Autowired
    private QuestionRepo questionRepo;


    public    Category getCategoryMethod(Long catId){
        return this.categoryRepo.findById(catId).orElseThrow(()->new ResourceNotFoundException("category","categoryId ",catId));
    }

    public Faculty getFacultyMethod(Long facultyId){
        return this.facultyRepo.findById(facultyId).orElseThrow(()->new ResourceNotFoundException("faculty","facultyId ",facultyId));
    }

    public User getUserMethod(Long userId){
        return this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","userId ",userId));
    }

    public Role getRoleMethod(Long roleId){
        return this.roleRepo.findById(roleId).orElseThrow(()->new ResourceNotFoundException("role","roleId ",roleId));
    }

    public Course getCourseMethod(Long courseId){
        return this.courseRepo.findById(courseId).orElseThrow(()->new ResourceNotFoundException("course","courseId ",courseId));
    }

    public Exam getExamMethod(Long examId){
        return this.examRepo.findById(examId).orElseThrow(()->new ResourceNotFoundException("exam","examId",examId));
    }

    public Question getQuestionMethod(Long qusId){
        return this.questionRepo.findById(qusId).orElseThrow(()->new ResourceNotFoundException("question","questionId",qusId));
    }
}
