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
    @Query(value = "select u.user_id,u.user_email,u.user_name,u.user_status,u.user_password,u.user_contact_number,u.deleted,u.is_enabled,u.user_date_of_birth,u.user_gender,u.user_roll_no from user_table u inner join user_role ur on u.user_id=ur.user_id_fk inner join role_table r on r.role_id=ur.role_id_fk where u.user_status='pending' and r.role_name='ROLE_TEACHER' ",nativeQuery = true)
    List<User> findPendingTeacher();
    @Query(value = "select u.user_id,u.user_email,u.user_name,u.user_status,u.user_password,u.user_contact_number,u.deleted,u.is_enabled,u.user_date_of_birth,u.user_gender,u.user_roll_no from user_table u inner join user_role ur on u.user_id=ur.user_id_fk inner join role_table r on r.role_id=ur.role_id_fk where u.user_status='pending' and r.role_name='ROLE_STUDENT'",nativeQuery = true)
    List<User> findPendingStudent();
    @Query(value = "select u.user_id,u.user_email,u.user_name,u.user_status,u.user_password,u.user_contact_number,u.deleted,u.is_enabled,u.user_date_of_birth,u.user_gender,u.user_roll_no from user_table u inner join user_role ur on u.user_id=ur.user_id_fk inner join role_table r on r.role_id=ur.role_id_fk where u.user_status='approved' and r.role_name='ROLE_TEACHER' ",nativeQuery = true)
    List<User> findApprovedTeacher();
    @Query(value = "select u.user_id,u.user_email,u.user_name,u.user_status,u.user_password,u.user_contact_number,u.deleted,u.is_enabled,u.user_date_of_birth,u.user_gender,u.user_roll_no from user_table u inner join user_role ur on u.user_id=ur.user_id_fk inner join role_table r on r.role_id=ur.role_id_fk where u.user_status='approved' and r.role_name='ROLE_STUDENT' ",nativeQuery = true)
    List<User> findApprovedStudent();
    @Query(value = "select count(u.user_id) from user_table u inner join user_role ur on ur.user_id_fk=u.user_id inner  join role_table r on r.role_id=ur.role_id_fk where r.role_name='ROLE_TEACHER' and u.user_status='approved'",nativeQuery = true)
    Integer countTeacher();
    @Query(value = "select count(u.user_id) from user_table u inner join user_role ur on ur.user_id_fk=u.user_id inner  join role_table r on r.role_id=ur.role_id_fk where r.role_name='ROLE_STUDENT' and u.user_status='approved'",nativeQuery = true)
    Integer countStudent();
    @Query(value = "select c.course_id from user_table u inner join user_role ur on ur.user_id_fk=u.user_id inner join role_table r on r.role_id=ur.role_id_fk  inner join user_course uc on uc.user_id_fk=u.user_id inner join course_table c on c.course_id=uc.course_id_fk where r.role_name='ROLE_STUDENT' and u.user_email=?1",nativeQuery = true)
    Long findCourseIdByStudent(String email);
    @Query(value = "select r.role_name from user_table u inner join user_role ur on ur.user_id_fk=u.user_id inner join role_table r on r.role_id=ur.role_id_fk where u.user_email=?1",nativeQuery = true)
    String findUserRoleName(String email);
    @Query(value = "select u.user_id from user_table as u join user_role as ur on u.user_id=ur.user_id_fk join role_table as r on r.role_id=ur.role_id_fk where r.role_name='ROLE_STUDENT'",nativeQuery = true)
    List<Long> findAllStudentId();
}
