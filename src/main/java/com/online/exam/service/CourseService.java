package com.online.exam.service;

import com.online.exam.dto.CourseDto;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface CourseService {
    CourseDto createCourse(CourseDto courseDto, Principal principal) throws Exception;
    String deleteCourse(Long courseId,Principal principal) throws Exception;
    String updateCourse(CourseDto courseDto,Principal principal) throws Exception;

    CourseDto getCourseById(Long courseId) throws Exception;
    List<CourseDto> getAllCourse();









}
