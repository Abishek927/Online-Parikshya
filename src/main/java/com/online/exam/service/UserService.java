package com.online.exam.service;

import com.online.exam.dto.UserDto;
import com.online.exam.model.Role;
import com.online.exam.model.User;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserService  {


    Map<Integer,String> createUser(UserDto userDto) throws Exception;

    UserDto getUserByEmail(String email);

    String deleteUser(Long userId);

    UserDto getUserDto(User user);

   // UserDto updateUser(Long userId,UserDto updatedUser) throws Exception;
    List<User> getAllUser();

    String approveUser(Long userId) throws MessagingException;
    String approveAllUser();

    String rejectUser(Long userId) throws MessagingException;
    String rejectAll();

List<UserDto> viewAllApprovedTeacher();
List<UserDto> viewAllApprovedStudent();


    List<UserDto> viewPendingTeacher();
    List<UserDto> viewPendingStudent();


}
