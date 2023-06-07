package com.online.exam.dto;

import com.online.exam.model.UserStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
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
    private String userStatus;
    private LocalDate userDateOfBirth;
    private String roleName;
    private Long courseId;
    private Set<Long> courseSet;

}
