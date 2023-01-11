package com.online.exam.service.impl;

import com.online.exam.exception.ResourceNotFoundException;
import com.online.exam.model.Role;
import com.online.exam.model.User;
import com.online.exam.model.userRole;
import com.online.exam.repo.RoleRepo;
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



    @Override
    public User createUser(User user, List<Role> roles) throws Exception {
        List<userRole> userRoleList=new ArrayList<>();
        userRole userRole=new userRole();


        User user1 = this.userRepo.findByUserEmail(user.getUserEmail());
        List<User> user2=new ArrayList<>();

        List<Role> assignedRole=new ArrayList<>();
        if (user1 != null) {
            throw new Exception("User already exist with the given useremail:" + user.getUserEmail());
        } else {
            for (Role eachRole : roles
            ) {
                Role role = this.roleRepo.findByRoleName(eachRole.getRoleName());

                if (role == null) {
                    throw new Exception("User does not provide valid role for registration!!!");
                } else {
                    userRole.setUser(user);
                    userRole.setRole(role);
                    userRoleList.add(userRole);
                    role.setUserRoles(userRoleList);
                    user.setUserRoles(userRoleList);


                    user1 = this.userRepo.save(user);


                }
            }


            return user1;
        }


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
        User retrievedUser=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","userId "+userId,null));
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
        User retrievedUser=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","userId "+userId,null));
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





