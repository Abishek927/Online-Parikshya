package com.online.exam.repo;

import com.online.exam.model.Course;
import com.online.exam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface CourseRepo extends JpaRepository<Course,Long> {


    Course findByCourseTitle(String title);
    @Query(value = "select count(c.course_id) from course_table c inner join user_course uc on uc.course_id_fk=c.course_id inner join user_table u on u.user_id=uc.user_id_fk where uc.user_id_fk=?1",nativeQuery = true)
    Integer countCourseByUsers(Long userId);
    @Query(value = "select c.course_id,c.course_created,c.course_desc,c.course_title,c.deleted from course_table c inner join user_course uc on uc.course_id_fk=c.course_id inner join user_table u on u.user_id=uc.user_id_fk inner join user_role ur on ur.user_id_fk=u.user_id inner join role_table rt on rt.role_id=ur.role_id_fk where rt.role_name='ROLE_TEACHER' and u.user_email=?1",nativeQuery = true)
    Course findCourseByTeacher(String email);

}
