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
    private QueryHelper queryHelper;
    @Override
    public FacultyDto createFaculty(Long userId, FacultyDto facultyDto) throws Exception {

        User retrievedUser=queryHelper.getUserMethod(userId);

        Faculty retrievedFaculty=this.facultyRepo.findByFacultyName(facultyDto.getFacultyName());
        if(retrievedFaculty!=null){
            throw new Exception("Faculty with the given faculty name "+facultyDto.getFacultyName()+"already exists!!");
        }
        else{
                        retrievedFaculty=this.modelMapper.map(facultyDto,Faculty.class);

                        retrievedUser.getFaculties().add(retrievedFaculty);
                        retrievedFaculty.setUsers(Set.of(retrievedUser));



                        retrievedFaculty=this.facultyRepo.save(retrievedFaculty);

                    }





        return this.modelMapper.map(retrievedFaculty,FacultyDto.class);
    }

    @Override
    public String deleteFaculty(Long userId,String facultyName) {
        User retrievedUser=this.queryHelper.getUserMethod(userId);

        Faculty faculty=this.facultyRepo.findByFacultyName(facultyName);
        if(faculty==null){
            return "there is no faculty exist with the given name";
        }else{
           Set<Faculty> faculties=retrievedUser.getFaculties();
           if(!faculties.isEmpty()) {
               for (Faculty eachFaculty:faculties
                    ) {
                   if(eachFaculty.getFacultyId().equals(faculty.getFacultyId())){
                       //eachFaculty.getUsers().remove(retrievedUser);
                       this.facultyRepo.deleteById(faculty.getFacultyId());
               }
           }









            }
           else{
               return "there is no faculty created by the given user with id "+retrievedUser.getUserId();
           }

        }


        return "faculty deleted successfully";
    }

    @Override
    public FacultyDto updateFaculty(Long userId, Long facultyId, FacultyDto facultyDto) throws Exception {
        FacultyDto facultyDto1=new FacultyDto();
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty=this.queryHelper.getFacultyMethod(facultyId);
        Set<Faculty> faculties=retrievedUser.getFaculties();
        if(!faculties.isEmpty()){
            for (Faculty eachFaculty:faculties
                 ) {
                if(eachFaculty.getFacultyId().equals(retrievedFaculty.getFacultyId())){
                    if(facultyDto.getFacultyName()!=null) {
                        retrievedFaculty.setFacultyName(facultyDto.getFacultyName());
                    }
                    if(facultyDto.getFacultyDesc()!=null) {
                        retrievedFaculty.setFacultyDesc(facultyDto.getFacultyDesc());
                    }
                     Faculty faculty=this.facultyRepo.save(retrievedFaculty);
                     facultyDto1.setFacultyId(faculty.getFacultyId());
                     facultyDto1.setFacultyName(faculty.getFacultyName());
                     facultyDto1.setFacultyDesc(faculty.getFacultyDesc());

                }
            }

        }else {
            throw new Exception("there is no faculties created by the given user!!!");
        }










        return facultyDto1;
    }

    @Override
    public FacultyDto getFacultyByName(Long userId, String name) {
        FacultyDto resultDto=new FacultyDto();

        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty resultFaculty=this.facultyRepo.findByFacultyName(name);
        Set<Faculty> faculties=retrievedUser.getFaculties();
        if(!faculties.isEmpty()){
            for (Faculty eachFaculty:faculties
                 ) {
                if(eachFaculty.getFacultyId().equals(resultFaculty.getFacultyId())){
                    if(resultFaculty!=null) {
                        resultDto.setFacultyId(resultFaculty.getFacultyId());
                        resultDto.setFacultyName(resultFaculty.getFacultyName());
                        resultDto.setFacultyDesc(resultFaculty.getFacultyDesc());


                    }
                    else {
                        return null;
                    }
                    }
            }
        }







        return resultDto;
    }

    @Override
    public List<FacultyDto> getAllFaculty(Long userId) {
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Set<Faculty> faculties=retrievedUser.getFaculties();
        if(!faculties.isEmpty()){
            return faculties.stream().map(faculty -> this.modelMapper.map(faculty,FacultyDto.class)).collect(Collectors.toList());
        }


        return null;
    }


}
