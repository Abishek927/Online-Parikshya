package com.online.exam.service.impl;

import com.online.exam.dto.CourseDto;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.repo.CourseRepo;
import com.online.exam.repo.UserRepo;
import com.online.exam.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private QueryHelper queryHelper;

    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepo userRepo;


    @Transactional
    @Override
    public CourseDto createCourse(CourseDto courseDto,Principal principal) throws Exception {
        User loggedInUser=userRepo.findByUserEmail(principal.getName());
            Set<Course> retrievedCourse=loggedInUser.getCourses();
           if(!retrievedCourse.isEmpty()) {
               for (Course eachCourse : retrievedCourse
               ) {
                   Course redundantCourse = this.courseRepo.findByCourseTitle(courseDto.getCourseTitle());
                   if (redundantCourse != null) {
                       throw new Exception("Course with the given title already exist in the given category");
                   }
               }
           }
                        Course course = new Course();
                        course.setCourseTitle(courseDto.getCourseTitle());
                        course.setCourseDesc(courseDto.getCourseDesc());
                        course.setCourseCreated(new Date());
                        course.setUsers(Set.of(loggedInUser));
                        loggedInUser.getCourses().add(course);
                        this.courseRepo.save(course);
                        return courseDto;
    }

    @Transactional
    @Override
    public String deleteCourse(Long courseId, Principal principal){
        String message = "";
        User user = userRepo.findByUserEmail(principal.getName());
        Course course = queryHelper.getCourseMethod(courseId);
        if (course.getUsers().contains(user)) {
            course.setUsers(null);
            user.getCourses().remove(course);
            List<Exam> exams = course.getExams();
            if (!exams.isEmpty()) {
                for (Exam eachExam : exams
                ) {
                    eachExam.setCourse(null);

                }
            }
            this.courseRepo.deleteById(course.getCourseId());
            message = "course with the id " + courseId + " deleted successfully";
            return message;
        }
        return message;
    }

    @Transactional
        @Override
        public String updateCourse(CourseDto courseDto,Principal principal) throws Exception{
                Course retrievedCourse=queryHelper.getCourseMethod(courseDto.getCourseId());
                User loggedInUser=userRepo.findByUserEmail(principal.getName());
                if(loggedInUser.getCourses().contains(retrievedCourse)&&retrievedCourse.getUsers().contains(loggedInUser)){
                if (!courseDto.getCourseTitle().isEmpty()) {
                    retrievedCourse.setCourseTitle(courseDto.getCourseTitle());
                }
                if (!courseDto.getCourseDesc().isEmpty()) {
                    retrievedCourse.setCourseDesc(courseDto.getCourseDesc());
                }
                this.courseRepo.save(retrievedCourse);


            }
                return "Course updated successfully!!!";
        }


    @Override
    public CourseDto getCourseById(Long courseId) throws Exception {
        return this.modelMapper.map(courseRepo.getReferenceById(courseId),CourseDto.class);
    }

    @Override
    public List<CourseDto> getAllCourse() {
        List<Course> courses=courseRepo.findAll();
        return courses.stream().map(course -> this.modelMapper.map(course,CourseDto.class)).collect(Collectors.toList());
    }




}