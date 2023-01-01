package com.online.exam.service.impl;

import com.online.exam.exception.ResourceNotFoundException;
import com.online.exam.model.User;
import com.online.exam.model.UserRole;
import com.online.exam.repo.RoleRepo;
import com.online.exam.repo.UserRepo;
import com.online.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;
    @Override
    public User createUser(User user, List<UserRole> userRoleList) throws Exception {

        User localUser=this.userRepo.findByUserName(user.getUserName());
        if(localUser!=null)
        {
            System.out.println("User with that username is already existed...!!");
            throw new Exception("user already exist!!!");
        }
        else{
            for (UserRole userRole:userRoleList
                 ) {
                this.roleRepo.save(userRole.getRole());
                user.setUserRoles(userRoleList);
                user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
                userRole.setUser(user);
                localUser=this.userRepo.save(user);


            }


        }



        return localUser;
    }

    @Override
    public User getUserByUserName(String username) {
            User resultUser=this.userRepo.findByUserName(username);
            if(resultUser!=null){
                return resultUser;
            }else {
                throw new ResourceNotFoundException("user", "userName", username);
            }

    }

    @Override
    public String deleteUser(Long id) {
        User user=this.userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user","userId"+id,""));
        String message=null;
        if(user!=null){
            user.getUserRoles().add(null);
            user.setUserRoles(null);
            this.userRepo.delete(user);
            message="User with userId "+id+" deleted successfully";
        }


        return message;
    }

    @Override
    public User updateUser(Long userId, User user) {
        User searchedUser=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("username","userId "+userId,null));
        if(searchedUser!=null){
            searchedUser.setUserName(user.getUserName());
            searchedUser.setUserPassword(user.getUserPassword());
            searchedUser.setUserEmail(user.getUserEmail());
            searchedUser.setUserFirstName(user.getUserFirstName());
            searchedUser.setUserLastName(user.getUserLastName());
            searchedUser.setUserPhoneNumber(user.getUserPhoneNumber());
            this.userRepo.save(searchedUser);
        }

        return searchedUser;
    }
}
