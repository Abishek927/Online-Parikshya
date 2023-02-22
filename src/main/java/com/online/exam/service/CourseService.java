package com.online.exam.service;

import com.online.exam.dto.CourseDto;
import com.online.exam.model.Course;
import com.online.exam.model.Faculty;

import java.util.List;

public interface CourseService {
    CourseDto createCourse(Long userId, Long facultyId, Long categoryId, CourseDto courseDto) throws Exception;
    String deleteCourse(Long userId,Long facultyId,Long categoryId,Long courseId) throws Exception;
    CourseDto updateCourse(Long userId,Long facultyId,Long categoryId,Long courseId,CourseDto courseDto) throws Exception;
    List<CourseDto> getCoursesByCategory(Long userId, Long facultyId, Long categoryId) throws Exception;
    CourseDto getCourseById(Long userId,Long facultyId,Long categoryId,Long courseId) throws Exception;
    List<CourseDto> getAllCourse(Long userId);





}
