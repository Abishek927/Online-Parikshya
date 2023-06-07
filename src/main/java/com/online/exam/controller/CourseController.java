package com.online.exam.controller;

import com.online.exam.dto.CourseDto;
import com.online.exam.helper.ApiResponse;
import com.online.exam.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('manage_course')")
    ResponseEntity<?> createCourseController(@RequestBody CourseDto courseDto, Principal principal) throws Exception {
                CourseDto course1=this.courseService.createCourse(courseDto,principal);
                if(course1==null){
                    return new ResponseEntity<>(new ApiResponse("Something went wrong!!!",false), HttpStatusCode.valueOf(500));
                }
                return new ResponseEntity<>(course1,HttpStatusCode.valueOf(200));
    }


    @DeleteMapping("/delete/{courseId}")
    @PreAuthorize("hasAuthority('manage_course')")
    ResponseEntity<ApiResponse> deleteCourseController(@PathVariable Long courseId,Principal principal) throws Exception {
        String message=this.courseService.deleteCourse(courseId,principal);
        if(message.isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Something went wrong!!!!",false),HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));

    }


    @PutMapping("/update")
    @PreAuthorize("hasAuthority('manage_course')")
    ResponseEntity<?> updateCourseController(@RequestBody CourseDto courseDto,Principal principal) throws Exception {
            String message=this.courseService.updateCourse(courseDto,principal);
            return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));
    }
    @GetMapping("/read/{cId}")
    ResponseEntity<?> getCourseByIdController(@PathVariable Long cId) throws Exception {
        CourseDto courseDto=this.courseService.getCourseById(cId);
        return new ResponseEntity<>(courseDto,HttpStatusCode.valueOf(200));
    }
    @GetMapping("/readAll")
    ResponseEntity<?> getAllCourse()
    {
        Map<String,List<CourseDto>> message=new HashMap<>();
        List<CourseDto> courseDtos=this.courseService.getAllCourse();
        if(!courseDtos.isEmpty()){
            message.put("data",courseDtos);
            return new ResponseEntity<>(message,HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(new ApiResponse("there is no courses!!!",false),HttpStatusCode.valueOf(200));
    }


    @GetMapping("/read/count")
    @PreAuthorize("hasAuthority('manage_course')")
    ResponseEntity<Map<String,Integer>> getCourseCount(Principal principal){
        Integer countValue=courseService.countCourseByUser(principal);
        Map<String,Integer> message=new HashMap<>();
        if(countValue==0){
            message.put("No course created by user!!!",countValue);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
        message.put("there is course created by the user",countValue);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }




}
