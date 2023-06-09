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
    @Query(value = "select count(u.user_id) from user_table u inner join user_role ur on ur.user_id_fk=u.user_id inner  join role_table r on r.role_id=ur.role_id_fk inner join role_authority ra on ra.role_id_fk=r.role_id inner join authorities_table a on a.authority_id=ra.authority_id_fk where a.authority_name='ROLE_TEACHER' and u.status='approved'",nativeQuery = true)
    Integer countTeacher();
}
