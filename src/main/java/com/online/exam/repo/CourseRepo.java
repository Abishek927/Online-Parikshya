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
    @Query(value = "select count(c) from Course c where c.users=?1",nativeQuery = true)
    Integer countCourseByUsers(Set<User> users);

}
