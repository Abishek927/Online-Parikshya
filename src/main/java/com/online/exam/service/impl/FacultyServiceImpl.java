package com.online.exam.service.impl;

import com.online.exam.dto.FacultyDto;
import com.online.exam.exception.ResourceNotFoundException;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.repo.FacultyRepo;
import com.online.exam.repo.RoleRepo;
import com.online.exam.repo.UserRepo;
import com.online.exam.service.FacultyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    @Autowired
    private FacultyRepo facultyRepo;
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepo userRepo;


    @Autowired
    private QueryHelper queryHelper;
    @Override
    public FacultyDto createFaculty(Principal principal,FacultyDto facultyDto) throws Exception {
        User retrievedUser=queryHelper.getUserMethod(facultyDto.getUserId());
        User retrievedUser1=userRepo.findByUserEmail(principal.getName());
        if(retrievedUser.getUserId().equals(retrievedUser1.getUserId())) {
            Faculty retrievedFaculty = this.facultyRepo.findByFacultyName(facultyDto.getFacultyName());
            if (retrievedFaculty != null) {
                throw new Exception("Faculty with the given faculty name " + facultyDto.getFacultyName() + "already exists!!");
            } else {
                retrievedFaculty = this.modelMapper.map(facultyDto, Faculty.class);

                retrievedUser.getFaculties().add(retrievedFaculty);
                retrievedFaculty.setUsers(Set.of(retrievedUser));


                retrievedFaculty = this.facultyRepo.save(retrievedFaculty);

            }
            return this.modelMapper.map(retrievedFaculty,FacultyDto.class);
        }
        return null;
    }

    @Override
    public String deleteFaculty(String facultyName,Principal principal) {
        User retrievedUser=userRepo.findByUserEmail(principal.getName());
        Faculty faculty=this.facultyRepo.findByFacultyName(facultyName);
            if (faculty == null) {
                return "there is no faculty exist with the given name";
            } else {
                if(faculty.getUsers().contains(retrievedUser)) {
                    Set<Faculty> faculties = retrievedUser.getFaculties();
                    for (Faculty eachFaculty : faculties
                        ) {
                            if (eachFaculty.getFacultyId().equals(faculty.getFacultyId())) {
                                //eachFaculty.getUsers().remove(retrievedUser);
                                this.facultyRepo.deleteById(faculty.getFacultyId());
                            }
                        }
                }

            }
            return "faculty deleted successfully";
    }

    @Override
    public FacultyDto updateFaculty(FacultyDto facultyDto,Principal principal) throws Exception {
        User retrievedUser=userRepo.findByUserEmail(principal.getName());
        Faculty retrievedFaculty=this.queryHelper.getFacultyMethod(facultyDto.getFacultyId());
        if(retrievedFaculty.getUsers().contains(retrievedUser)) {
            Set<Faculty> faculties = retrievedUser.getFaculties();
            for (Faculty eachFaculty : faculties
            ) {
                if (eachFaculty.getFacultyId().equals(retrievedFaculty.getFacultyId())) {
                    if (facultyDto.getFacultyName() != null) {
                        retrievedFaculty.setFacultyName(facultyDto.getFacultyName());
                    }
                    if (facultyDto.getFacultyDesc() != null) {
                        retrievedFaculty.setFacultyDesc(facultyDto.getFacultyDesc());
                    }
                    Faculty faculty = this.facultyRepo.save(retrievedFaculty);
                    facultyDto.setFacultyId(faculty.getFacultyId());
                }
            }
        }
        return facultyDto;
    }

    @Override
    public FacultyDto getFacultyByName(String name,Principal principal) {
        User retrievedUser=userRepo.findByUserEmail(principal.getName());
        FacultyDto resultDto=new FacultyDto();
        Faculty resultFaculty=this.facultyRepo.findByFacultyName(name);
        if(resultFaculty!=null) {
            if (resultFaculty.getUsers().contains(retrievedUser)) {
                resultDto.setFacultyId(resultFaculty.getFacultyId());
                resultDto.setFacultyName(resultFaculty.getFacultyName());
                resultDto.setFacultyDesc(resultFaculty.getFacultyDesc());
            }
        }
        return resultDto;
    }

    @Override
    public List<FacultyDto> getAllFaculty() {
        List<Faculty> faculties=facultyRepo.findAll();
            if(!faculties.isEmpty()) {
                return faculties.stream().map(faculty -> this.modelMapper.map(faculty, FacultyDto.class)).collect(Collectors.toList());
            }

        return null;
    }


}
