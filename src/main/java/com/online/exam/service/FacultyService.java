package com.online.exam.service;

import com.online.exam.dto.FacultyDto;


import java.util.List;

public interface FacultyService {

    FacultyDto createFaculty(Long userId, FacultyDto facultyDto) throws Exception;
    String deleteFaculty(Long userId,String facultyName);

    FacultyDto updateFaculty(Long userId,Long facultyId,FacultyDto facultyDto) throws Exception;

    FacultyDto getFacultyByName(Long userId,String name);
    List<FacultyDto> getAllFaculty(Long userId);
}
