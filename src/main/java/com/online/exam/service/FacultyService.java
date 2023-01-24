package com.online.exam.service;

import com.online.exam.model.Faculty;
import com.online.exam.model.Role;

import java.util.List;

public interface FacultyService {

    Faculty createFaculty(Long userId,Faculty faculty) throws Exception;
    String deleteFaculty(Long userId,String facultyName);

    Faculty updateFaculty(Long userId,Long facultyId,Faculty faculty);

    Faculty getFacultyByName(Long userId,String name);
    List<Faculty> getFacultyByUser(Long userId);
}
