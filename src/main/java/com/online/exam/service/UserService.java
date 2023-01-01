package com.online.exam.service;

import com.online.exam.model.User;
import com.online.exam.model.UserRole;

import java.util.List;

public interface UserService {

    User createUser(User user, List<UserRole> userRoleList) throws Exception;
    User getUserByUserName(String username);
    String deleteUser(Long id);

    User updateUser(Long userId,User user);

}
