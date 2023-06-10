package com.online.exam.controller;

import com.online.exam.model.User;
import com.online.exam.repo.CourseRepo;
import com.online.exam.repo.UserRepo;
import com.online.exam.service.CourseService;
import com.online.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/count")
public class CountController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CourseRepo courseRepo;
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('view_count')")
    ResponseEntity<Map<String,Map<String,Object>>> countEntity(Principal principal){
        Map<String,Map<String,Object>> message=new HashMap<>();
        User loggedInUser=userRepo.findByUserEmail(principal.getName());
        Map<String,Object> map=new HashMap<>();
       map.put("TeacherCount",userRepo.countTeacher());
       map.put("StudentCount",userRepo.countStudent());
       map.put("CourseCount",courseRepo.countCourseByUsers(loggedInUser.getUserId()));
       message.put("data",map);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }


}
