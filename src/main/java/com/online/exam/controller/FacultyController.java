package com.online.exam.controller;

import com.online.exam.dto.FacultyDto;
import com.online.exam.helper.ApiResponse;


import com.online.exam.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class FacultyController {
    @Autowired
    private FacultyService facultyService;
    @PostMapping("/user/{userId}/faculty/create")
    @PreAuthorize("hasAuthority('manage_faculty')")
    ResponseEntity<?> createFacultyController(@PathVariable("userId")Long userId,@RequestBody FacultyDto faculty) throws Exception {

        FacultyDto resultFaculty=this.facultyService.createFaculty(userId,faculty);
        if(resultFaculty==null){
            return new ResponseEntity<>(new ApiResponse("faculty creation is failed!!Something went wrong!!",false), HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(resultFaculty,HttpStatusCode.valueOf(200));

    }

    @DeleteMapping("/user/{userId}/faculty/delete")
    @PreAuthorize("hasAuthority('manage_faculty')")
    ResponseEntity<ApiResponse> deleteFacultyController(@PathVariable("userId")Long userId,@RequestParam("facultyName")String facultyName){
        String responseMessage=this.facultyService.deleteFaculty(userId,facultyName);
        if(responseMessage.contains("successfully")){
            return new ResponseEntity<>(new ApiResponse(responseMessage,true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(new ApiResponse(responseMessage,false),HttpStatusCode.valueOf(200));
    }

    @PutMapping("/user/{userId}/faculty/update/{facultyId}")
    @PreAuthorize("hasAuthority('manage_faculty')")
    ResponseEntity<?> updateFacultyController(@PathVariable("userId")Long userId,@PathVariable("facultyId")Long facultyId,@RequestBody FacultyDto faculty) throws Exception {
        FacultyDto updateFaculty=this.facultyService.updateFaculty(userId,facultyId,faculty);
        if(updateFaculty.equals(null)) {
            return new ResponseEntity<>(new ApiResponse("Something went wrong!!", false), HttpStatusCode.valueOf(500));
        }
            return new ResponseEntity<>(updateFaculty,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/user/{userId}/faculty/read")
    @PreAuthorize("hasAuthority('manage_faculty')")
    ResponseEntity<?> getFacultyByNameController(@PathVariable("userId")Long userId,@RequestParam("facultyName")String facultyName){
        FacultyDto facultyByName=this.facultyService.getFacultyByName(userId,facultyName);
        if(facultyByName.equals(null)){
            return new ResponseEntity<>(new ApiResponse("there is no faculty with the given name",false),HttpStatusCode.valueOf(500));

        }
        return new ResponseEntity<>(facultyByName,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/faculty/getAll")
    @PreAuthorize("hasAuthority('manage_faculty')")
    ResponseEntity<?> getAllFacultyController(@RequestParam("userId")Long userId){
        List<FacultyDto> facultyDtos=this.facultyService.getAllFaculty(userId);
        if(facultyDtos!=null){
            return new ResponseEntity<>(facultyDtos,HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(new ApiResponse("there is no faculties!!!",false),HttpStatusCode.valueOf(200));
    }




}
