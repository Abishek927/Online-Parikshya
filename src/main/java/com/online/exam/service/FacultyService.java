package com.online.exam.service;

import com.online.exam.dto.FacultyDto;


import java.security.Principal;
import java.util.List;

public interface FacultyService {

    FacultyDto createFaculty(Principal principal,FacultyDto facultyDto) throws Exception;
    String deleteFaculty(String facultyName,Principal principal);

    FacultyDto updateFaculty(FacultyDto facultyDto,Principal principal) throws Exception;

    FacultyDto getFacultyByName(String name,Principal principal);
    List<FacultyDto> getAllFaculty();
}
