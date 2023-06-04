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
    @Query(value = "select u.user_id,u.user_email,u.user_name,u.user_status,u.user_password,u.user_contact_number,u.deleted,u.is_enabled,u.user_date_of_birth,u.user_gender,u.user_roll_no from user_table u where u.user_status=?1",nativeQuery = true)
    List<User> findPendingTeacher(String status);
    @Query(value = "select u.user_id,u.user_email,u.user_name,u.user_status,u.user_password,u.user_contact_number,u.deleted,u.is_enabled,u.user_date_of_birth,u.user_gender,u.user_roll_no from user_table u where u.user_status=?1",nativeQuery = true)
    List<User> findPendingStudent(String status);
}
