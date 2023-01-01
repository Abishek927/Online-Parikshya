package com.online.exam.controller;

import com.online.exam.helper.ApiResponse;
import com.online.exam.model.Role;
import com.online.exam.model.User;
import com.online.exam.model.UserRole;
import com.online.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    ResponseEntity<User> createUser(@RequestBody User user) throws Exception {
        List<UserRole> userRoleList=new ArrayList<UserRole>();
        UserRole userRole=new UserRole();
        Role role=new Role();
        role.setRoleName("Admin");
        role.setRoleId(89L);
        role.setUserRoles(userRoleList);
        userRole.setUser(user);
        userRole.setRole(role);

        userRoleList.add(userRole);

        User resultUser=this.userService.createUser(user,userRoleList);
        return new ResponseEntity<User>(resultUser,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/read/{userName}")
    ResponseEntity<User> getUserByUserName(@PathVariable("userName")String userName){
        User user=this.userService.getUserByUserName(userName);
        return new ResponseEntity<User>(user,HttpStatusCode.valueOf(200));


    }
    @DeleteMapping("/delete")
    ResponseEntity<ApiResponse> deleteUser(@RequestParam("userId")Long userId ){
        String message=this.userService.deleteUser(userId);
        return new ResponseEntity<ApiResponse>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));


    }

    @PutMapping("/update/{userId}")
    ResponseEntity<User> updateUser(@PathVariable("userId")Long userId,@RequestBody User user){
                        User updatedUser=this.userService.updateUser(userId,user);
                        return new ResponseEntity<User>(updatedUser,HttpStatusCode.valueOf(200));

    }



}
