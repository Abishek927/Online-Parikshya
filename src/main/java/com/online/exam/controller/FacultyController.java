/*
package com.online.exam.controller;

import com.online.exam.helper.ApiResponse;
import com.online.exam.model.Faculty;

import com.online.exam.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class FacultyController {
    @Autowired
    private FacultyService facultyService;
    @PostMapping("/user/{userId}/faculty/create")
    ResponseEntity<?> createFacultyController(@PathVariable("userId")Long userId,@RequestBody Faculty faculty) throws Exception {

        Faculty resultFaculty=this.facultyService.createFaculty(userId,faculty);
        if(resultFaculty==null){
            return new ResponseEntity<>(new ApiResponse("faculty creation is failed!!Something went wrong!!",false), HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(resultFaculty,HttpStatusCode.valueOf(200));

    }

    @DeleteMapping("/user/{userId}/faculty/delete")
    ResponseEntity<ApiResponse> deleteFacultyController(@PathVariable("userId")Long userId,@RequestParam("facultyName")String facultyName){
        String responseMessage=this.facultyService.deleteFaculty(userId,facultyName);
        if(responseMessage.contains("successfully")){
            return new ResponseEntity<>(new ApiResponse(responseMessage,true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(new ApiResponse(responseMessage,false),HttpStatusCode.valueOf(500));
    }

    @PutMapping("/user/{userId}/faculty/update/{facultyId}")
    ResponseEntity<?> updateFacultyController(@PathVariable("userId")Long userId,@PathVariable("facultyId")Long facultyId,@RequestBody Faculty faculty){
        Faculty updateFaculty=this.facultyService.updateFaculty(userId,facultyId,faculty);
        if(updateFaculty.equals(null)) {
            return new ResponseEntity<>(new ApiResponse("Something went wrong!!", false), HttpStatusCode.valueOf(500));
        }
            return new ResponseEntity<>(updateFaculty,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/user/{userId}/faculty/read")
    ResponseEntity<?> getFacultyByNameController(@PathVariable("userId")Long userId,@RequestParam("facultyName")String facultyName){
        Faculty facultyByName=this.facultyService.getFacultyByName(userId,facultyName);
        if(facultyByName.equals(null)){
            return new ResponseEntity<>(new ApiResponse("there is no faculty with the given name",false),HttpStatusCode.valueOf(500));

        }
        return new ResponseEntity<>(facultyByName,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/user/{userId}/faculty/readByUser")
    ResponseEntity<?> getFacultyByUser(@PathVariable("userId")Long userId){
        List<Faculty> faculties =this.facultyService.getFacultyByUser(userId);
        if(faculties.equals(null)){
            return new ResponseEntity<>(new ApiResponse("there is no faculty created by the given user",false),HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(faculties,HttpStatusCode.valueOf(200));
    }


}
*/
