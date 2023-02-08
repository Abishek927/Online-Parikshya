/*
package com.online.exam.service.impl;

import com.online.exam.exception.ResourceNotFoundException;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.repo.FacultyRepo;
import com.online.exam.repo.RoleRepo;
import com.online.exam.repo.UserRepo;
import com.online.exam.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {
    @Autowired
    private FacultyRepo facultyRepo;
    @Autowired
    private RoleRepo roleRepo;


    @Autowired
    private QueryHelper queryHelper;
    @Override
    public Faculty createFaculty(Long userId,Faculty faculty) throws Exception {

        User retrievedUser=queryHelper.getUserMethod(userId);
        List<userRole> userRoles=retrievedUser.getUserRoles();
        Faculty retrievedFaculty=this.facultyRepo.findByFacultyName(faculty.getFacultyName());
        if(retrievedFaculty!=null){
            throw new Exception("Faculty with the given faculty name "+faculty.getFacultyName()+"already exists!!");
        }
        else{
            for (userRole eachUserRole:userRoles
                 ) {
                Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
                if(role==null){
                    throw new Exception("there is no role registered with the given role name!!");
                }
                else{
                    if(role.getRoleName().equalsIgnoreCase("admin")){
                        List<UserFaculty> userFaculties=new ArrayList<>();
                        UserFaculty userFaculty=new UserFaculty();
                        userFaculty.setUser(retrievedUser);
                        userFaculty.setFaculty(faculty);
                        userFaculties.add(userFaculty);
                        faculty.setUserFaculties(userFaculties);

                        retrievedFaculty=this.facultyRepo.save(faculty);

                    }
                }
            }
        }


        return retrievedFaculty;
    }

    @Override
    public String deleteFaculty(Long userId,String facultyName) {
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        List<userRole> userRoles=retrievedUser.getUserRoles();
        Faculty faculty=this.facultyRepo.findByFacultyName(facultyName);
        if(faculty==null){
            return "there is no faculty exist with the given name";
        }else{
            for (userRole eachUserRole:userRoles
                 ) {
                Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
                if(role.getRoleName().equalsIgnoreCase("admin")){
                    List<UserFaculty> userFaculties=faculty.getUserFaculties();
                    for (UserFaculty eachUserFaculty:userFaculties
                         ) {
                        eachUserFaculty.setFaculty(null);
                        eachUserFaculty.setUser(null);

                        this.facultyRepo.deleteById(faculty.getFacultyId());

                    }


                }

            }

        }


        return "faculty deleted successfully";
    }

    @Override
    public Faculty updateFaculty(Long userId, Long facultyId, Faculty faculty) {
        Faculty updatedFaculty=new Faculty();
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty=this.queryHelper.getFacultyMethod(facultyId);
        List<userRole> userRoles=retrievedUser.getUserRoles();
        for (userRole eachUserRole:userRoles
             ) {
            Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
            if(role.getRoleName().equalsIgnoreCase("admin")){
                retrievedFaculty.setFacultyName(faculty.getFacultyName());
                retrievedFaculty.setFacultyDesc(faculty.getFacultyDesc());
               updatedFaculty =this.facultyRepo.save(retrievedFaculty);
            }

        }



        return updatedFaculty;
    }

    @Override
    public Faculty getFacultyByName(Long userId, String name) {
        Faculty resultFaculty=new Faculty();
        User retrievedUser=this.queryHelper.getUserMethod(userId);
       List<userRole>userRoles= retrievedUser.getUserRoles();
        for (userRole eachUserRole:userRoles
             ) {
            Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
            if(role.getRoleName().equalsIgnoreCase("admin")||role.getRoleName().equalsIgnoreCase("tacher")||role.getRoleName().equals("student")){
                 resultFaculty=this.facultyRepo.findByFacultyName(name);
            }

        }

        return resultFaculty;
    }

    @Override
    public List<Faculty> getFacultyByUser(Long userId) {
        List<Faculty> faculties=new ArrayList<>();
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        List<userRole> userRoles=retrievedUser.getUserRoles();
        for (userRole eachUserRole:userRoles
             ) {
            Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
            if(role.getRoleName().equalsIgnoreCase("admin")){
               faculties=this.facultyRepo.findAll();
            }

        }
        return faculties;
    }
}
*/
