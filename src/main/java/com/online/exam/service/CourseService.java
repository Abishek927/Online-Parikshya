package com.online.exam.service;

import com.online.exam.dto.CourseDto;
import com.online.exam.model.Course;
import com.online.exam.model.Faculty;

import java.security.Principal;
import java.util.List;

public interface CourseService {
    CourseDto createCourse(CourseDto courseDto, Principal principal) throws Exception;
    String deleteCourse(Long courseId,Principal principal) throws Exception;
    CourseDto updateCourse(CourseDto courseDto,Principal principal) throws Exception;
    List<CourseDto> getCoursesByCategory(Long categoryId) throws Exception;
    CourseDto getCourseById(Long courseId) throws Exception;
    List<CourseDto> getAllCourse(Principal principal);





}
