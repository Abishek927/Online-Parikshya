package com.online.exam.repo;

import com.online.exam.model.User;
import com.online.exam.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    User findByUserEmail(String userEmail);
    User findByUserName(String userName);
    @Query(value = "select u.userEmail,u.userContactNumber,u.userName,u.userStatus,u.userPassword,u.userContactNumber from User u where u.userStatus=?1",nativeQuery = true)
    List<User> findPendingTeacher(String status);
    @Query(value = "select u.userEmail,u.userName,u.userStatus,u.userPassword,u.userGender,u.userDateOfBirth,u.userContactNumber from User u where u.userStatus=?1",nativeQuery = true)
    List<User> findPendingStudent(String status);
}
