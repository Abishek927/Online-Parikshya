package com.online.exam.service;

import com.online.exam.dto.CourseDto;
import com.online.exam.model.Course;
import com.online.exam.model.Faculty;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface CourseService {
    CourseDto createCourse(CourseDto courseDto, Principal principal) throws Exception;
    String deleteCourse(Long courseId,Principal principal) throws Exception;
    String updateCourse(CourseDto courseDto,Principal principal) throws Exception;
    List<CourseDto> getCoursesByCategory(Long categoryId);
    CourseDto getCourseById(Long courseId) throws Exception;
    List<CourseDto> getAllCourse(Principal principal);
    Integer countCourseByUser(Principal principal);

    Map<Integer,List<CourseDto>> getCoursesByCategoryForStudent(Principal principal);






}
