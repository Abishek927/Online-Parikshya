package com.online.exam.controller;

import com.online.exam.dto.CourseDto;
import com.online.exam.helper.ApiResponse;
import com.online.exam.model.Course;

import com.online.exam.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping("/user/{uId}/fac/{facId}/cat/{catId}/course/create")
    ResponseEntity<?> createCourseController(@PathVariable Long uId, @PathVariable Long facId, @PathVariable Long catId, @RequestBody CourseDto courseDto) throws Exception {
                Course course1=this.courseService.createCourse(uId,facId,catId,courseDto);
                if(course1==null){
                    return new ResponseEntity<>(new ApiResponse("Something went wrong!!!",false), HttpStatusCode.valueOf(500));
                }
                return new ResponseEntity<>(course1,HttpStatusCode.valueOf(200));
    }


    @DeleteMapping("/user/{uId}/fac/{fId}/cat/{catId}/course/delete")
    ResponseEntity<ApiResponse> deleteCourseController(@PathVariable Long uId,@PathVariable Long fId,@PathVariable Long catId,@RequestParam("courseId")Long courseId) throws Exception {
        String message=this.courseService.deleteCourse(uId, fId, catId, courseId);
        if(message.isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Something went wrong!!!!",false),HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));

    }


}
