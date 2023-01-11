package com.online.exam.service;

import com.online.exam.model.Role;
import com.online.exam.model.User;

import java.util.List;

public interface UserService {


    User createUser(User user, List<Role> roles) throws Exception;

    User getUserByEmail(String email);

    String deleteUser(Long userId);

    User updateUser(Long userId,User updatedUser);

}
