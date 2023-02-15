package com.online.exam.dto;

import com.online.exam.model.UserStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class UserDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private Long userRollNo;
    private boolean enabled;
    private String userContactNumber;
    private String userGender;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    private String userDateOfBirth;
    private Set<RoleDto> roleDtoSet;
    private FacultyDto facultyDto;
    private CategoryDto categoryDto;
    private CourseDto courseDto;
}
