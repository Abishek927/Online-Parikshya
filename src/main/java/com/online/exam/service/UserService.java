package com.online.exam.service;

import com.online.exam.dto.UserDto;
import com.online.exam.model.Role;
import com.online.exam.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService  {


    UserDto createUser(UserDto userDto, Long facId, Long catId, Long courseId,Long roleId) throws Exception;

    UserDto getUserByEmail(String email);

    String deleteUser(Long userId);

   // UserDto updateUser(Long userId,UserDto updatedUser) throws Exception;
    List<User> getAllUser();

    String approveUser(Long userId);
    String approveAllUser();

    String rejectUser(Long userId);
    String rejectAll();

    List<UserDto> viewAllApprovedStudent();
    List<UserDto> viewAllApprovedTeacher();


}
