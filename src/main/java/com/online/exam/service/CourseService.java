package com.online.exam.service;

import com.online.exam.model.Course;
import com.online.exam.model.Faculty;

import java.util.List;

public interface CourseService {
    Course createCourse(Long userId,Long facultyId,Long categoryId,Course course) throws Exception;
    String deleteCourse(Long userId,Long facultyId,Long categoryId,Long courseId) throws Exception;
    Course updateCourse(Long userId,Long facultyId,Long categoryId,Long courseId,Course course) throws Exception;
    List<Course> getCoursesByCategory(Long userId, Long facultyId, Long categoryId);
    Course getCourseByName(Long userId,Long facultyId,Long categoryId,String name);




}
