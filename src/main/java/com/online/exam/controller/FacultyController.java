package com.online.exam.controller;

import com.online.exam.dto.FacultyDto;
import com.online.exam.helper.ApiResponse;
import com.online.exam.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
@RestController
@RequestMapping("/faculty")
public class FacultyController {
    @Autowired
    private FacultyService facultyService;
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('manage_faculty')")
    ResponseEntity<?> createFacultyController(@RequestBody FacultyDto faculty, Principal principal) throws Exception {

        FacultyDto resultFaculty=this.facultyService.createFaculty(principal,faculty);
        if(resultFaculty==null){
            return new ResponseEntity<>(new ApiResponse("faculty creation is failed!!Something went wrong!!",false), HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(resultFaculty,HttpStatusCode.valueOf(200));

    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('manage_faculty')")
    ResponseEntity<ApiResponse> deleteFacultyController(@RequestParam("facultyName")String facultyName,Principal principal){
        String responseMessage=this.facultyService.deleteFaculty(facultyName,principal);
        if(responseMessage.contains("successfully")){
            return new ResponseEntity<>(new ApiResponse(responseMessage,true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(new ApiResponse(responseMessage,false),HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('manage_faculty')")
    ResponseEntity<?> updateFacultyController(@RequestBody FacultyDto faculty,Principal principal) throws Exception {
        FacultyDto updateFaculty=this.facultyService.updateFaculty(faculty,principal);
        if(updateFaculty.equals(null)) {
            return new ResponseEntity<>(new ApiResponse("Something went wrong!!", false), HttpStatusCode.valueOf(500));
        }
            return new ResponseEntity<>(updateFaculty,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/read")
    @PreAuthorize("hasAuthority('manage_faculty')")
    ResponseEntity<?> getFacultyByNameController(@RequestParam("facultyName")String facultyName,Principal principal){
        FacultyDto facultyByName=this.facultyService.getFacultyByName(facultyName,principal);
        if(facultyByName.equals(null)){
            return new ResponseEntity<>(new ApiResponse("there is no faculty with the given name",false),HttpStatusCode.valueOf(500));

        }
        return new ResponseEntity<>(facultyByName,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/getAll")
    ResponseEntity<?> getAllFacultyController(){
        List<FacultyDto> facultyDtos=this.facultyService.getAllFaculty();
        if(facultyDtos!=null){
            return new ResponseEntity<>(facultyDtos,HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(new ApiResponse("there is no faculties!!!",false),HttpStatusCode.valueOf(200));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority('manage_faculty')")
    ResponseEntity<?> countController(Principal principal){
        Integer integer=this.facultyService.countFaculties(principal);
        return ResponseEntity.status(HttpStatus.OK).body(integer);
    }




}
