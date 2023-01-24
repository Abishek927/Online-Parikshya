package com.online.exam.service.impl;

import com.online.exam.exception.ResourceNotFoundException;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.model.Category;
import com.online.exam.repo.RoleRepo;
import com.online.exam.repo.CategoryRepo;
import com.online.exam.repo.UserRepo;
import com.online.exam.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


@Service
@Data
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;


    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private QueryHelper queryHelper;



    @Override
    public User createUser(User user,Long catId, List<Role> roles) throws Exception {
        List<userRole> userRoleList = new ArrayList<>();
        userRole userRole = new userRole();
        Category category = new Category();
        User user1=new User();
        if (catId > 0) {

            category = queryHelper.getCategoryMethod(catId);
        }
             user1 = this.userRepo.findByUserEmail(user.getUserEmail());

            if (user1 != null) {
                throw new Exception("User already exist with the given useremail:" + user.getUserEmail());
            } else {
                for (Role eachRole : roles
                ) {
                    Role role = this.roleRepo.findByRoleName(eachRole.getRoleName());

                    if (role == null) {
                        throw new Exception("User does not provide valid role for registration!!!");
                    } else {
                        if (role.getRoleName().equalsIgnoreCase("student")) {
                            if (category != null) {
                                List<UserFaculty> userFaculties=new ArrayList<>();
                                UserFaculty userFaculty=new UserFaculty();

                                Faculty faculty =category.getFaculty();
                                userFaculty.setFaculty(faculty);
                                userFaculties.add(userFaculty);

                                user.setCategory(category);
                                user.setUserFaculties(userFaculties);
                            }

                        }


                        userRole.setUser(user);
                        userRole.setRole(role);
                        userRoleList.add(userRole);
                        role.setUserRoles(userRoleList);
                        user.setUserRoles(userRoleList);


                        user1 = this.userRepo.save(user);


                    }
                }
            }



            return user1;



    }

    @Override
    public User getUserByEmail(String email) {
        User retrievedUser=this.userRepo.findByUserEmail(email);
        if(retrievedUser==null){
            throw new ResourceNotFoundException("user","email "+email,null);
        }


        return retrievedUser;
    }

    @Override
    public String deleteUser(Long userId) {
        String message="";
        User retrievedUser=queryHelper.getUserMethod(userId);
        List<userRole> userRoles=retrievedUser.getUserRoles();
        for (userRole eachUserRole:userRoles
             ) {
            eachUserRole.setUser(null);
            eachUserRole.setRole(null);


        }




        this.userRepo.delete(retrievedUser);
        message="User deleted successfully";
        return message;
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {
        User retrievedUser=queryHelper.getUserMethod(userId);
        retrievedUser.setUserName(updatedUser.getUserName());
        retrievedUser.setUserEmail(updatedUser.getUserEmail());
        retrievedUser.setUserPassword(updatedUser.getUserPassword());
        retrievedUser.setUserGender(updatedUser.getUserGender());
        retrievedUser.setUserRollNo(updatedUser.getUserRollNo());
        retrievedUser.setUserContactNumber(updatedUser.getUserContactNumber());
        retrievedUser.setUserDateOfBirth(updatedUser.getUserDateOfBirth());
        retrievedUser=this.userRepo.save(retrievedUser);

        return retrievedUser;
    }

}





